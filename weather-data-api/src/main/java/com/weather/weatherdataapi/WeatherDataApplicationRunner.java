package com.weather.weatherdataapi;

import com.weather.weatherdataapi.service.CoronaService;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("run::서버를 시작합니다. OpenAPI를 사용하여 DB의 데이터를 갱신합니다.");
        coronaService.fetchAndStoreCoronaInfoUsingOpenApi();

        log.info("run::성공적으로 갱신했습니다.");
    }

}
