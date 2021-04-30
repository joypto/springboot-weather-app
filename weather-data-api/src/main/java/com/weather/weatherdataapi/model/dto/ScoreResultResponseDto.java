package com.weather.weatherdataapi.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreResultResponseDto {

    private int coronaResult;
    private int pm10Result;
    private int pm25Result;
    private int tempResult;
    private int rainPerResult;
    private int weatherResult;
    private int humidityResult;
    private int windResult;
    private int uvResult;
    private int pollenRiskResult;
    private int coldResult;
    private int asthmaResult;
    private int foodPoisonResult;

}
