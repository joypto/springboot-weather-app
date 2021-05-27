package com.weather.weatherdataapi.service.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.HealthPollenRiskInfo;
import com.weather.weatherdataapi.model.vo.redis.HealthPollenRiskRedisVO;
import com.weather.weatherdataapi.repository.info.HealthPollenRiskInfoRepository;
import com.weather.weatherdataapi.repository.redis.HealthPollenRiskRedisRepository;
import com.weather.weatherdataapi.util.openapi.health.HealthApi;
import com.weather.weatherdataapi.util.openapi.health.pollen_risk.PollenRiskItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HealthPollenRiskService {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH");

    private final HealthPollenRiskInfoRepository healthPollenRiskInfoRepository;
    private final HealthPollenRiskRedisRepository healthPollenRiskRedisRepository;
    private final HealthApi healthApi;

    public static LocalDate convertDateFromString(String dateString) {
        return LocalDate.parse(dateString, DATETIME_FORMATTER);
    }

    public HealthPollenRiskInfo getInfoByBigRegion(BigRegion bigRegion) {
        Optional<HealthPollenRiskRedisVO> queriedRedisVO = healthPollenRiskRedisRepository.findById(bigRegion.getAdmCode());

        if (queriedRedisVO.isPresent()) {
            HealthPollenRiskInfo info = new HealthPollenRiskInfo(queriedRedisVO.get(), bigRegion);
            return info;
        }

        Optional<HealthPollenRiskInfo> queriedInfo = healthPollenRiskInfoRepository.findFirstByBigRegionOrderByDateDesc(bigRegion);

        // 기존에 저장되어 있는 정보가 전혀 없다면 원격 서버에서 가져옵니다.
        if (queriedInfo.isPresent() == false) {
            HealthPollenRiskInfo fetchedInfo = getInfoUsingOpenApi(bigRegion);
            saveAndCache(fetchedInfo);
            return fetchedInfo;
        }

        HealthPollenRiskInfo info = queriedInfo.get();

        // 기존에 저장되어 있는 정보가 있다면
        // 오늘자 데이터인지 확인하고, 오늘자 데이터라면 바로 반환합니다.
        if (checkIsTodayDataAlreadyExist(info))
            return info;

        // 그렇지 않다면 최신 데이터가 저장되어 있는지 확인하고, 없다면 저장 후 반환합니다.
        HealthPollenRiskInfo fetchedInfo = getInfoUsingOpenApi(bigRegion);
        if (checkIsLatestDataAlreadyExist(info, fetchedInfo) == false) {
            saveAndCache(fetchedInfo);
            return fetchedInfo;
        }

        // DB에 저장되어 있던 데이터는 오늘자의 데이터는 아니지만 원격 서버가 제공하는 가장 최신 데이터입니다.
        // 저장되어 있던 데이터를 반환합니다.
        return info;

    }

    public HealthPollenRiskInfo getInfoUsingOpenApi(BigRegion bigRegion) {
        LocalDate date = LocalDate.now();

        try {
            PollenRiskItem item = healthApi.getPollenRiskResponse(bigRegion.getAdmCode(), date);
            return new HealthPollenRiskInfo(item, bigRegion);
        } catch (Exception e) {
            log.error("금일 데이터를 받아오는 데 실패하였습니다.");
            log.error("전일 데이터로 요청합니다.");

            PollenRiskItem item = healthApi.getPollenRiskResponse(bigRegion.getAdmCode(), date.minusDays(1));
            return new HealthPollenRiskInfo(item, bigRegion);
        }
    }

    private boolean checkIsTodayDataAlreadyExist(HealthPollenRiskInfo info) {
        if (info.getDate().isBefore(LocalDate.now()))
            return false;

        return true;
    }

    private boolean checkIsLatestDataAlreadyExist(HealthPollenRiskInfo existed, HealthPollenRiskInfo fetched) {
        if (existed.getDate().isBefore(fetched.getDate()))
            return false;

        return true;
    }

    private void saveAndCache(HealthPollenRiskInfo info) {
        healthPollenRiskInfoRepository.save(info);

        HealthPollenRiskRedisVO redisVO = new HealthPollenRiskRedisVO(info);
        healthPollenRiskRedisRepository.save(redisVO);
    }

}
