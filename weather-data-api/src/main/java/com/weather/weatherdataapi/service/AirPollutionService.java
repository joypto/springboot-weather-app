package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.entity.AirPollution;
import com.weather.weatherdataapi.model.entity.Region;
import com.weather.weatherdataapi.repository.AirPollutionRepository;
import com.weather.weatherdataapi.repository.RegionRepository;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaStationUtil;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionItem;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionOpenApi;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationItem;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationOpenApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AirPollutionService {

    private final AirPollutionRepository airPollutionRepository;

    private final AirKoreaAirPollutionOpenApi airKoreaAirPollutionOpenApi;
    private final AirKoreaStationOpenApi airKoreaStationOpenApi;
    private final AirKoreaStationUtil airKoreaStationUtil;

    private final RegionRepository regionRepository;

    public AirPollution fetchAndStoreAirPollutionInfoUsingOpenApi(String stationName, Region region) {
        Optional<AirKoreaAirPollutionItem> fetchedResponse = airKoreaAirPollutionOpenApi.getResponseByStationName(stationName);

        if (fetchedResponse.isPresent() == false) {
            return null;
        }

        AirKoreaAirPollutionItem response = fetchedResponse.get();

        AirPollution airPollution = new AirPollution(response, region);
        airPollutionRepository.save(airPollution);

        region.updateAirPollution(airPollution);

        return airPollution;
    }

    public AirPollution getInfoByRegion(Region region) {
        String stationName = airKoreaStationUtil.getNearestStationNameByRegion(region);

        return fetchAndStoreAirPollutionInfoUsingOpenApi(stationName, region);
    }

    public String getStationNameUsingCoords(String tmX, String tmY) {
        Optional<AirKoreaStationItem> fetchedRespense = airKoreaStationOpenApi.getResponseItem(tmX, tmY);

        if (fetchedRespense.isPresent() == false)
            return null;

        return fetchedRespense.get().getStationName();
    }
}
