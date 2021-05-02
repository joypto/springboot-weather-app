package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.repository.info.AirPollutionInfoRepository;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaStationUtil;
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

    private final AirKoreaAirPollutionApi airKoreaAirPollutionOpenApi;
    private final AirKoreaStationApi airKoreaStationOpenApi;
    private final AirKoreaStationUtil airKoreaStationUtil;

    public AirPollutionInfo fetchAndStoreAirPollutionInfoUsingOpenApi(SmallRegion smallRegion) {
        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegion(smallRegion);
        Optional<AirKoreaAirPollutionItem> fetchedResponse = airKoreaAirPollutionOpenApi.getResponseByStationName(nearestStationName);

        if (fetchedResponse.isPresent() == false) {
            return null;
        }

        AirKoreaAirPollutionItem response = fetchedResponse.get();

        AirPollutionInfo airPollution = new AirPollutionInfo(response, smallRegion);
        airPollutionRepository.save(airPollution);

        return airPollution;
    }

    public AirPollutionInfo getInfoByRegion(SmallRegion smallRegion) {
        String stationName = airKoreaStationUtil.getNearestStationNameByRegion(smallRegion);

        return fetchAndStoreAirPollutionInfoUsingOpenApi(smallRegion);
    }

    public String getStationNameUsingCoords(String tmX, String tmY) {
        Optional<AirKoreaStationItem> fetchedRespense = airKoreaStationOpenApi.getResponseItem(tmX, tmY);

        if (fetchedRespense.isPresent() == false)
            return null;

        return fetchedRespense.get().getStationName();
    }

    public void calculateScore(ScoreResultResponseDto responseDto, AirPollutionInfo airPollution) {
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

        responseDto.setPm10Result(pm10Score);
        responseDto.setPm25Result(pm25Score);
    }
}
