package com.weather.weatherdataapi;

import com.weather.weatherdataapi.service.CoronaService;
import com.weather.weatherdataapi.service.LivingHealthService;
import com.weather.weatherdataapi.service.RegionService;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaStationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherDataApplicationRunner implements ApplicationRunner {

    private final CoronaService coronaService;
    private final LivingHealthService livingHealthService;

    private final RegionService regionService;

    private final AirKoreaStationUtil airKoreaStationUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("run::OpenAPI를 사용하여 DB의 데이터를 갱신합니다.");
        coronaService.fetchAndStoreCoronaInfoUsingOpenApi();
        livingHealthService.fetchAndStoreInfoUsingOpenApi();
        log.info("run::성공적으로 갱신했습니다.");

        regionService.initialize();

        airKoreaStationUtil.InitializeRegionStationNameDict();
    }

}
