package com.weather.weatherdataapi;

import com.weather.weatherdataapi.util.DateTimeUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class WeatherDataApiApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(DateTimeUtil.TIMEZONE_ASIA_SEOUL);

        SpringApplication.run(WeatherDataApiApplication.class, args);
    }

}
