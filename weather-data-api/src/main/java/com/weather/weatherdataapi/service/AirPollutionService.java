package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.WeatherDataResponseDto;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.model.vo.redis.AirPollutionRedisVO;
import com.weather.weatherdataapi.repository.info.AirPollutionInfoRepository;
import com.weather.weatherdataapi.repository.redis.AirPollutionRedisRepository;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaStationUtil;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionApi;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionItem;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationApi;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AirPollutionService {

    private final AirPollutionInfoRepository airPollutionRepository;
    private final AirPollutionRedisRepository airPollutionRedisRepository;

    private final AirKoreaAirPollutionApi airKoreaAirPollutionOpenApi;
    private final AirKoreaStationApi airKoreaStationOpenApi;
    private final AirKoreaStationUtil airKoreaStationUtil;

    public void setInfoAndScore(SmallRegion smallRegion, ScoreResultResponseDto scoreResultResponseDto, WeatherDataResponseDto weatherDataResponseDto) {
        AirPollutionInfo airPollutionInfo = getInfoBySmallRegion(smallRegion);

        weatherDataResponseDto.setAirPollution(airPollutionInfo);

        convertInfoToScore(airPollutionInfo, scoreResultResponseDto);
    }

    public AirPollutionInfo getInfoBySmallRegion(SmallRegion smallRegion) {
        AirPollutionInfo airPollutionInfo;

        // 캐시된 데이터가 있다면 캐시된 데이터를 우선적으로 사용합니다.
        if (airPollutionRedisRepository.existsById(smallRegion.getSmallRegionName())) {
            AirPollutionRedisVO airPollutionRedisVO = airPollutionRedisRepository.findById(smallRegion.getSmallRegionName()).orElseThrow(() -> new RuntimeException());
            airPollutionInfo = new AirPollutionInfo(airPollutionRedisVO, smallRegion);
        }
        // 그렇지 않다면 OpenApi를 사용하여 값을 가져옵니다.
        else {
            airPollutionInfo = fetchAndStoreAirPollutionInfoUsingOpenApi(smallRegion);

            AirPollutionRedisVO airPollutionRedisVO = new AirPollutionRedisVO(airPollutionInfo);
            airPollutionRedisRepository.save(airPollutionRedisVO);
        }

        return airPollutionInfo;
    }

    public AirPollutionInfo fetchAndStoreAirPollutionInfoUsingOpenApi(SmallRegion smallRegion) {
        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegion(smallRegion);
        AirKoreaAirPollutionItem response = airKoreaAirPollutionOpenApi.getResponseByStationName(nearestStationName);

        AirPollutionInfo airPollution = new AirPollutionInfo(response, smallRegion);
        airPollutionRepository.save(airPollution);

        return airPollution;
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

    private boolean checkAlreadyHasLatestData(SmallRegion smallRegion) {
        AirPollutionInfo latestData = airPollutionRepository.findFirstBySmallRegionOrderByCreatedAtDesc(smallRegion);

        if (latestData == null)
            return false;

        // 데이터 갱신 여부는 시간 단위 이상의 값으로만 판별합니다.
        // 분 단위 이하의 값은 고려하지 않습니다.
        LocalDateTime latestDataTime = LocalDateTime.of(
                latestData.getDateTime().getYear(),
                latestData.getDateTime().getMonth(),
                latestData.getDateTime().getDayOfMonth(),
                latestData.getDateTime().getHour(),
                0,
                0,
                0
        );

        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegion(smallRegion);
        AirKoreaAirPollutionItem latestFetchedData = airKoreaAirPollutionOpenApi.getResponseByStationName(nearestStationName);
        LocalDateTime latestFetchedDataTime = LocalDateTime.parse(latestFetchedData.getDataTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));

        if (latestDataTime.isBefore(latestFetchedDataTime))
            return false;

        return true;
    }

}
