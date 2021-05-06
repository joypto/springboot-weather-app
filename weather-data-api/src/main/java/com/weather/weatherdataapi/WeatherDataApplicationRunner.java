package com.weather.weatherdataapi;

import com.weather.weatherdataapi.service.CoronaService;
import com.weather.weatherdataapi.service.LivingHealthService;
import com.weather.weatherdataapi.service.RedisService;
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

    private final RedisService redisService;

    private final CoronaService coronaService;
    private final LivingHealthService livingHealthService;

    private final RegionService regionService;

    private final AirKoreaStationUtil airKoreaStationUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("run::서버를 시작하기 전 초기화를 진행합니다.");

        redisService.initialize();

        regionService.initialize();

        coronaService.fetchAndStoreCoronaInfoUsingOpenApi();
        livingHealthService.fetchAndStoreInfoUsingOpenApi();

        airKoreaStationUtil.InitializeRegionStationNameDict();

        log.info("run::초기화를 마쳤습니다.");
    }

}
