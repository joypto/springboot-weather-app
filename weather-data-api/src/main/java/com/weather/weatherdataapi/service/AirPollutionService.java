package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
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
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationApi;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AirPollutionService {

    private final AirPollutionInfoRepository airPollutionRepository;
    private final AirPollutionRedisRepository airPollutionRedisRepository;

    private final AirKoreaAirPollutionApi airKoreaAirPollutionOpenApi;
    private final AirKoreaStationApi airKoreaStationOpenApi;
    private final AirKoreaStationUtil airKoreaStationUtil;
    private final AirKoreaUtil airKoreaUtil;

    public void setInfoAndScore(SmallRegion smallRegion, ScoreResultResponseDto scoreResultResponseDto, TotalDataResponseDto weatherDataResponseDto) {
        AirPollutionInfo airPollutionInfo = getInfoBySmallRegion(smallRegion);

        weatherDataResponseDto.setAirPollution(airPollutionInfo);

        convertInfoToScore(airPollutionInfo, scoreResultResponseDto);
    }

    public AirPollutionInfo getInfoBySmallRegion(SmallRegion smallRegion) {

        // 캐시된 데이터가 있다면 캐시된 데이터를 우선적으로 사용합니다.
        if (airPollutionRedisRepository.existsById(smallRegion.getAdmCode())) {
            AirPollutionRedisVO airPollutionRedisVO = airPollutionRedisRepository.findById(smallRegion.getAdmCode()).orElseThrow(() -> new RuntimeException());
            AirPollutionInfo cachedInfo = new AirPollutionInfo(airPollutionRedisVO, smallRegion);

            return cachedInfo;
        }
        // 그렇지 않다면 최신 정보를 가져옵니다.
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
     *
     * @return 원격 서버에서 제공하는 최신 정보와 완전히 일치하는 정보입니다.
     */
    private AirPollutionInfo getSyncedLatestInfo(SmallRegion smallRegion) {
        AirPollutionInfo existedInfo = airPollutionRepository.findFirstBySmallRegionOrderByCreatedAtDesc(smallRegion);
        AirKoreaAirPollutionItem fetchedItem = fetchUsingOpenApi(smallRegion);

        // DB에 해당 지역에 대한 어떠한 대기오염 정보도 저장되어 있지 않거나,
        // 또는 최신 데이터가 아닐 경우 DB에 저장합니다.
        if (existedInfo == null || airKoreaUtil.checkLatestInfoAlreadyExists(existedInfo, fetchedItem) == false) {
            AirPollutionInfo newInfo = new AirPollutionInfo(fetchedItem, smallRegion);
            airPollutionRepository.save(newInfo);

            return newInfo;
        }

        // DB에 이미 최신 정보가 저장되어 있는 경우, 기존의 값을 반환합니다.
        return existedInfo;
    }

    private AirKoreaAirPollutionItem fetchUsingOpenApi(SmallRegion smallRegion) {
        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegion(smallRegion);
        AirKoreaAirPollutionItem response = airKoreaAirPollutionOpenApi.getResponseByStationName(nearestStationName);
        return response;
    }

    public String getStationNameUsingCoords(String tmX, String tmY) {
        Optional<AirKoreaStationItem> fetchedRespense = airKoreaStationOpenApi.getResponseItem(tmX, tmY);

        if (fetchedRespense.isPresent() == false)
            return null;

        return fetchedRespense.get().getStationName();
    }

    public void convertInfoToScore(AirPollutionInfo airPollution, ScoreResultResponseDto scoreResultResponseDto) {
        final int PM10_GOOD = 30;
        final int PM10_NORMAL = 80;
        final int PM10_BAD = 150;

        final int PM25_GOOD = 15;
        final int PM25_NORMAL = 35;
        final int PM25_BAD = 75;

        int pm10Score;
        if (airPollution.getPm10Value() <= PM10_GOOD)
            pm10Score = 100;
        else if (airPollution.getPm10Value() <= PM10_NORMAL)
            pm10Score = 70;
        else if (airPollution.getPm10Value() <= PM10_BAD)
            pm10Score = 40;
        else
            pm10Score = 10;

        int pm25Score;
        if (airPollution.getPm25Value() <= PM25_GOOD)
            pm25Score = 100;
        else if (airPollution.getPm25Value() <= PM25_NORMAL)
            pm25Score = 70;
        else if (airPollution.getPm25Value() <= PM25_BAD)
            pm25Score = 40;
        else
            pm25Score = 10;

        scoreResultResponseDto.setPm10Result(pm10Score);
        scoreResultResponseDto.setPm25Result(pm25Score);
    }

    public boolean checkLatestDataAlreadyExistsByRegion(SmallRegion smallRegion) {
        AirPollutionInfo latestData = airPollutionRepository.findFirstBySmallRegionOrderByCreatedAtDesc(smallRegion);

        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegion(smallRegion);
        AirKoreaAirPollutionItem latestFetchedData = airKoreaAirPollutionOpenApi.getResponseByStationName(nearestStationName);

        return airKoreaUtil.checkLatestInfoAlreadyExists(latestData, latestFetchedData);
    }

}
