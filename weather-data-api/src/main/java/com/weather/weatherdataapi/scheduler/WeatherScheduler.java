package com.weather.weatherdataapi.scheduler;

import com.weather.weatherdataapi.service.WeatherService;
import com.weather.weatherdataapi.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherScheduler {
    private final WeatherService weatherService;

    @Scheduled(cron = "0 0 0 * * ?", zone = DateTimeUtil.ZONE_NAME_ASIA_SEOUL)
    public void cronJobSch() {
        weatherService.fetchAndStoreWeatherInfoUsingOpenApi();
        log.info("날씨정보 캐시 초기화 완료");
    }
}
