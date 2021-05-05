package com.weather.weatherdataapi.scheduler;

import com.weather.weatherdataapi.service.LivingHealthService;
import com.weather.weatherdataapi.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherScheduler {
    private final WeatherService weatherService;

    @Scheduled(cron = "0 40 * * * ?")
    public void cronJobSch() {
            weatherService.fetchAndStoreWeatherInfoUsingOpenApi();
            log.info("fetchAndStoreWeatherInfo:: 날씨정보 캐시 초기화 완료");
    }
}
