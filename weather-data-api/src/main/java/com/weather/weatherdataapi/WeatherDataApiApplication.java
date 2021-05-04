package com.weather.weatherdataapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class WeatherDataApiApplication {
    //날씨정보를 받아오는 서버
    public static void main(String[] args) {
        SpringApplication.run(WeatherDataApiApplication.class, args);
    }

}
