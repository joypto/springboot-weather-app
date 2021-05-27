package com.weather.weatherdataapi.service.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.HealthFoodPoisonInfo;
import com.weather.weatherdataapi.model.vo.redis.HealthFoodPoisonRedisVO;
import com.weather.weatherdataapi.repository.info.HealthFoodPoisonInfoRepository;
import com.weather.weatherdataapi.repository.redis.HealthFoodPoisonRedisRepository;
import com.weather.weatherdataapi.util.openapi.health.HealthApi;
import com.weather.weatherdataapi.util.openapi.health.food_poison.FoodPoisonItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HealthFoodPoisonService {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH");

    private final HealthFoodPoisonInfoRepository healthFoodPoisonInfoRepository;
    private final HealthFoodPoisonRedisRepository healthFoodPoisonRedisRepository;
    private final HealthApi healthApi;

    public static LocalDate convertDateFromString(String dateString) {
        return LocalDate.parse(dateString, DATETIME_FORMATTER);
    }

    public HealthFoodPoisonInfo getInfoByBigRegion(BigRegion bigRegion) {
        Optional<HealthFoodPoisonRedisVO> queriedRedisVO = healthFoodPoisonRedisRepository.findById(bigRegion.getAdmCode());

        if (queriedRedisVO.isPresent()) {
            HealthFoodPoisonInfo info = new HealthFoodPoisonInfo(queriedRedisVO.get(), bigRegion);
            return info;
        }

        Optional<HealthFoodPoisonInfo> queriedInfo = healthFoodPoisonInfoRepository.findFirstByBigRegionOrderByDateDesc(bigRegion);

        // 기존에 저장되어 있는 정보가 전혀 없다면 원격 서버에서 가져옵니다.
        if (queriedInfo.isPresent() == false) {
            HealthFoodPoisonInfo fetchedInfo = getInfoUsingOpenApi(bigRegion);
            saveAndCache(fetchedInfo);
            return fetchedInfo;
        }

        HealthFoodPoisonInfo info = queriedInfo.get();

        // 기존에 저장되어 있는 정보가 있다면
        // 오늘자 데이터인지 확인하고, 오늘자 데이터라면 바로 반환합니다.
        if (checkIsTodayDataAlreadyExist(info))
            return info;

        // 그렇지 않다면 최신 데이터가 저장되어 있는지 확인하고, 없다면 저장 후 반환합니다.
        HealthFoodPoisonInfo fetchedInfo = getInfoUsingOpenApi(bigRegion);
        if (checkIsLatestDataAlreadyExist(info, fetchedInfo) == false) {
            saveAndCache(fetchedInfo);
            return fetchedInfo;
        }

        // DB에 저장되어 있던 데이터는 오늘자의 데이터는 아니지만 원격 서버가 제공하는 가장 최신 데이터입니다.
        // 저장되어 있던 데이터를 반환합니다.
        return info;

    }

    public HealthFoodPoisonInfo getInfoUsingOpenApi(BigRegion bigRegion) {
        LocalDate date = LocalDate.now();

        try {
            FoodPoisonItem item = healthApi.getFoodPoisonResponse(bigRegion.getAdmCode(), date);
            return new HealthFoodPoisonInfo(item, bigRegion);
        } catch (Exception e) {
            log.error("금일 데이터를 받아오는 데 실패하였습니다.");
            log.error("전일 데이터로 요청합니다.");

            FoodPoisonItem item = healthApi.getFoodPoisonResponse(bigRegion.getAdmCode(), date.minusDays(1));
            return new HealthFoodPoisonInfo(item, bigRegion);
        }
    }

    private boolean checkIsTodayDataAlreadyExist(HealthFoodPoisonInfo info) {
        if (info.getDate().isBefore(LocalDate.now()))
            return false;

        return true;
    }

    private boolean checkIsLatestDataAlreadyExist(HealthFoodPoisonInfo existed, HealthFoodPoisonInfo fetched) {
        if (existed.getDate().isBefore(fetched.getDate()))
            return false;

        return true;
    }

    private void saveAndCache(HealthFoodPoisonInfo info) {
        healthFoodPoisonInfoRepository.save(info);

        HealthFoodPoisonRedisVO redisVO = new HealthFoodPoisonRedisVO(info);
        healthFoodPoisonRedisRepository.save(redisVO);
    }

}
