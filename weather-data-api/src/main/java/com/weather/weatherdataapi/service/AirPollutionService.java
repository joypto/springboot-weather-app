package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.model.entity.Region;
import com.weather.weatherdataapi.repository.info.AirPollutionInfoRepository;
import com.weather.weatherdataapi.repository.RegionRepository;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaStationUtil;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionItem;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionApi;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationItem;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationApi;
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

    private final RegionRepository regionRepository;

    public AirPollutionInfo fetchAndStoreAirPollutionInfoUsingOpenApi(String stationName, Region region) {
        Optional<AirKoreaAirPollutionItem> fetchedResponse = airKoreaAirPollutionOpenApi.getResponseByStationName(stationName);

        if (fetchedResponse.isPresent() == false) {
            return null;
        }

        AirKoreaAirPollutionItem response = fetchedResponse.get();

        AirPollutionInfo airPollution = new AirPollutionInfo(response, region);
        airPollutionRepository.save(airPollution);

        region.updateAirPollution(airPollution);

        return airPollution;
    }

    public AirPollutionInfo getInfoByRegion(Region region) {
        String stationName = airKoreaStationUtil.getNearestStationNameByRegion(region);

        return fetchAndStoreAirPollutionInfoUsingOpenApi(stationName, region);
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
