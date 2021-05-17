package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.exception.repository.info.InvalidAirPollutionInfoException;
import com.weather.weatherdataapi.model.dto.ScoreResultDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.model.vo.redis.AirPollutionRedisVO;
import com.weather.weatherdataapi.repository.info.AirPollutionInfoRepository;
import com.weather.weatherdataapi.repository.redis.AirPollutionRedisRepository;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaStationUtil;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaUtil;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionApi;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AirPollutionService {

    private final AirPollutionInfoRepository airPollutionRepository;
    private final AirPollutionRedisRepository airPollutionRedisRepository;

    private final AirKoreaAirPollutionApi airKoreaAirPollutionOpenApi;
    private final AirKoreaStationUtil airKoreaStationUtil;
    private final AirKoreaUtil airKoreaUtil;

    public void setInfoAndScore(SmallRegion smallRegion, ScoreResultDto scoreResultDto, TotalDataResponseDto weatherDataResponseDto) {
        AirPollutionInfo airPollutionInfo = getInfoBySmallRegion(smallRegion);

        weatherDataResponseDto.setAirPollution(airPollutionInfo);

        convertInfoToScore(airPollutionInfo, scoreResultDto);
    }

    public AirPollutionInfo getInfoBySmallRegion(SmallRegion smallRegion) {
        Optional<AirPollutionRedisVO> queriedAirPollutionRedisVO = airPollutionRedisRepository.findById(smallRegion.getAdmCode());

        // 캐시된 데이터가 있다면 캐시된 데이터를 우선적으로 사용합니다.
        if (queriedAirPollutionRedisVO.isPresent()) {
            AirPollutionRedisVO airPollutionRedisVO = queriedAirPollutionRedisVO.get();
            AirPollutionInfo cachedInfo = new AirPollutionInfo(airPollutionRedisVO, smallRegion);

            return cachedInfo;
        }
        // 그렇지 않다면 최신 정보를 가져옵니다.
        // 또한 최신 정보는 캐싱합니다.
        else {
            AirPollutionInfo latestInfo = getSyncedLatestInfo(smallRegion);

            AirPollutionRedisVO airPollutionRedisVO = new AirPollutionRedisVO(latestInfo);

            airPollutionRedisRepository.save(airPollutionRedisVO);

            return latestInfo;
        }

    }

    /**
     * 원격 서버에서 제공하는 최신 정보와 일치하는 정보 객체를 반환합니다.
     * DB에 최신 정보가 저장되어 있지 않다면 최신 정보를 DB에도 저장합니다.
     * 만약 원격 서버에서 제공받을 수 없다면 차선택으로 DB에 이미 저장되어 있는 데이터 중 가장 최신의 정보를 반환합니다.
     *
     * @return 일반적으로 원격 서버에서 제공되는 최신 정보를 반환합니다.
     * 만약 원격 서버에서 제공받을 수 없다면 차선택으로 DB에 이미 저장되어 있는 데이터 중 가장 최신의 정보를 반환합니다.
     */
    private AirPollutionInfo getSyncedLatestInfo(SmallRegion smallRegion) {
        Optional<AirPollutionInfo> queriedExistedInfo = airPollutionRepository.findFirstBySmallRegionOrderByCreatedAtDesc(smallRegion);

        AirKoreaAirPollutionItem fetchedItem;
        try {
            fetchedItem = fetchUsingOpenApi(smallRegion);
        } catch (Exception e) {
            log.error(e.getMessage());

            fetchedItem = null;
        }

        // 원격 서버에서 가져온 데이터가 없을 경우, 기존에 DB에 있는 데이터 중 가장 최신의 정보를 가져옵니다.
        if (fetchedItem == null) {
            if (queriedExistedInfo.isPresent() == false)
                throw new RuntimeException("대기오염 정보를 가져올 수 없습니다. 원격 서버에서도 가져올 수 없었으며 DB에 이미 저장되어 있는 기존의 대기오염 정보가 전혀 없습니다.");

            return queriedExistedInfo.get();
        }
        // 원격 서버에서 가져온 데이터가 있는 경우, DB에
        else {

            // DB에 해당 지역에 대한 어떠한 대기오염 정보도 저장되어 있지 않거나,
            // 또는 최신 데이터가 아닐 경우 DB에 저장합니다.
            if (queriedExistedInfo.isPresent() == false
                    || airKoreaUtil.checkLatestInfoAlreadyExists(queriedExistedInfo.get(), fetchedItem) == false) {
                AirPollutionInfo newInfo = new AirPollutionInfo(fetchedItem, smallRegion);
                airPollutionRepository.save(newInfo);

                return newInfo;
            }

            // DB에 이미 최신 정보가 저장되어 있는 경우, 기존의 값을 반환합니다.
            return queriedExistedInfo.get();
        }

    }

    private AirKoreaAirPollutionItem fetchUsingOpenApi(SmallRegion smallRegion) {
        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegion(smallRegion);
        AirKoreaAirPollutionItem response = airKoreaAirPollutionOpenApi.getResponseByStationName(nearestStationName);
        return response;
    }

    public void convertInfoToScore(AirPollutionInfo airPollution, ScoreResultDto scoreResultDto) {
        final int PM10_LEVEL1 = 15;
        final int PM10_LEVEL2 = 30;
        final int PM10_LEVEL3 = 40;
        final int PM10_LEVEL4 = 50;
        final int PM10_LEVEL5 = 75;
        final int PM10_LEVEL6 = 100;
        final int PM10_LEVEL7 = 150;

        final int PM25_LEVEL1 = 8;
        final int PM25_LEVEL2 = 15;
        final int PM25_LEVEL3 = 20;
        final int PM25_LEVEL4 = 25;
        final int PM25_LEVEL5 = 37;
        final int PM25_LEVEL6 = 50;
        final int PM25_LEVEL7 = 75;

        int pm10Score = 0;
        int pm25Score = 0;

        Integer pm10Value = airPollution.getPm10Value();

        if (pm10Value != null) {
            if (pm10Value <= PM10_LEVEL1)
                pm10Score = 100;
            else if (pm10Value <= PM10_LEVEL2)
                pm10Score = 85;
            else if (pm10Value <= PM10_LEVEL3)
                pm10Score = 70;
            else if (pm10Value <= PM10_LEVEL4)
                pm10Score = 55;
            else if (pm10Value <= PM10_LEVEL5)
                pm10Score = 40;
            else if (pm10Value <= PM10_LEVEL6)
                pm10Score = 25;
            else if (pm10Value <= PM10_LEVEL7)
                pm10Score = 10;
            else
                pm10Score = 5;
        }

        Integer pm25Value = airPollution.getPm25Value();

        if (pm25Value != null) {
            if (pm25Value <= PM25_LEVEL1)
                pm25Score = 100;
            else if (pm25Value <= PM25_LEVEL2)
                pm25Score = 85;
            else if (pm25Value <= PM25_LEVEL3)
                pm25Score = 70;
            else if (pm25Value <= PM25_LEVEL4)
                pm25Score = 55;
            else if (pm25Value <= PM25_LEVEL5)
                pm25Score = 40;
            else if (pm25Value <= PM25_LEVEL6)
                pm25Score = 25;
            else if (pm25Value <= PM25_LEVEL7)
                pm25Score = 10;
            else
                pm25Score = 5;
        }

        scoreResultDto.setPm10Result(pm10Score);
        scoreResultDto.setPm25Result(pm25Score);
    }

    public boolean checkLatestDataAlreadyExistsByRegion(SmallRegion smallRegion) {
        AirPollutionInfo latestData = airPollutionRepository.findFirstBySmallRegionOrderByCreatedAtDesc(smallRegion).orElseThrow(() -> new InvalidAirPollutionInfoException());

        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegion(smallRegion);

        AirKoreaAirPollutionItem latestFetchedData = null;
        try {
            latestFetchedData = airKoreaAirPollutionOpenApi.getResponseByStationName(nearestStationName);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("원격 서버에서 최신 정보를 가져올 수 없습니다. 이미 최신 상태로 동기화되어 있다고 간주합니다.");

            return true;
        }

        return airKoreaUtil.checkLatestInfoAlreadyExists(latestData, latestFetchedData);
    }

    @Transactional
    public void refreshCache() {
        airPollutionRedisRepository.deleteAll();
    }

}
