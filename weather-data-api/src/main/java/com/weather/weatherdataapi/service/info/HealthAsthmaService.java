package com.weather.weatherdataapi.service.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.HealthAsthmaInfo;
import com.weather.weatherdataapi.model.vo.redis.HealthAsthmaRedisVO;
import com.weather.weatherdataapi.repository.info.HealthAsthmaInfoRepository;
import com.weather.weatherdataapi.repository.redis.HealthAsthmaRedisRepository;
import com.weather.weatherdataapi.util.openapi.health.HealthApi;
import com.weather.weatherdataapi.util.openapi.health.asthma.AsthmaItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HealthAsthmaService {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH");

    private final HealthAsthmaInfoRepository healthAsthmaInfoRepository;
    private final HealthAsthmaRedisRepository healthAsthmaRedisRepository;
    private final HealthApi healthApi;

    public static LocalDate convertDateFromString(String dateString) {
        return LocalDate.parse(dateString, DATETIME_FORMATTER);
    }

    public HealthAsthmaInfo getInfoByBigRegion(BigRegion bigRegion) {
        Optional<HealthAsthmaRedisVO> queriedRedisVO = healthAsthmaRedisRepository.findById(bigRegion.getAdmCode());

        if (queriedRedisVO.isPresent()) {
            HealthAsthmaInfo info = new HealthAsthmaInfo(queriedRedisVO.get(), bigRegion);
            return info;
        }

        Optional<HealthAsthmaInfo> queriedInfo = healthAsthmaInfoRepository.findFirstByBigRegionOrderByDateDesc(bigRegion);

        // 기존에 저장되어 있는 정보가 전혀 없다면 원격 서버에서 가져옵니다.
        if (queriedInfo.isPresent() == false) {
            HealthAsthmaInfo fetchedInfo = getInfoUsingOpenApi(bigRegion);
            saveAndCache(fetchedInfo);
            return fetchedInfo;
        }

        HealthAsthmaInfo info = queriedInfo.get();

        // 기존에 저장되어 있는 정보가 있다면
        // 오늘자 데이터인지 확인하고, 오늘자 데이터라면 바로 반환합니다.
        if (checkIsTodayDataAlreadyExist(info))
            return info;

        // 그렇지 않다면 최신 데이터가 저장되어 있는지 확인하고, 없다면 저장 후 반환합니다.
        HealthAsthmaInfo fetchedInfo = getInfoUsingOpenApi(bigRegion);
        if (checkIsLatestDataAlreadyExist(info, fetchedInfo) == false) {
            saveAndCache(fetchedInfo);
            return fetchedInfo;
        }

        // DB에 저장되어 있던 데이터는 오늘자의 데이터는 아니지만 원격 서버가 제공하는 가장 최신 데이터입니다.
        // 저장되어 있던 데이터를 반환합니다.
        return info;

    }

    public HealthAsthmaInfo getInfoUsingOpenApi(BigRegion bigRegion) {
        LocalDate date = LocalDate.now();

        try {
            AsthmaItem item = healthApi.getAsthmaResponse(bigRegion.getAdmCode(), date);
            return new HealthAsthmaInfo(item, bigRegion);
        } catch (Exception e) {
            log.error("금일 데이터를 받아오는 데 실패하였습니다.");
            log.error("전일 데이터로 요청합니다.");

            AsthmaItem item = healthApi.getAsthmaResponse(bigRegion.getAdmCode(), date.minusDays(1));
            return new HealthAsthmaInfo(item, bigRegion);
        }
    }

    private boolean checkIsTodayDataAlreadyExist(HealthAsthmaInfo info) {
        if (info.getDate().isBefore(LocalDate.now()))
            return false;

        return true;
    }

    private boolean checkIsLatestDataAlreadyExist(HealthAsthmaInfo existed, HealthAsthmaInfo fetched) {
        if (existed.getDate().isBefore(fetched.getDate()))
            return false;

        return true;
    }

    private void saveAndCache(HealthAsthmaInfo info) {
        healthAsthmaInfoRepository.save(info);

        HealthAsthmaRedisVO redisVO = new HealthAsthmaRedisVO(info);
        healthAsthmaRedisRepository.save(redisVO);
    }

}
