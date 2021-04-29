package com.weather.weatherdataapi;

import com.weather.weatherdataapi.service.CoronaService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WeatherDataApplicationRunner implements ApplicationRunner {

    private final CoronaService coronaService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        coronaService.fetchAndStoreCoronaInfoUsingOpenApi();
    }

}
