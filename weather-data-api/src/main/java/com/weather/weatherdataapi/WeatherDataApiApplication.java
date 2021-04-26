package com.weather.weatherdataapi;

import com.weather.weatherdataapi.corona.GovCoronaOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherDataApiApplication {
    //날씨정보를 받아오는 서버
    public static void main(String[] args) {
        SpringApplication.run(WeatherDataApiApplication.class, args);

        try {
            new GovCoronaOpenApi();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
