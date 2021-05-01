package com.weather.weatherdataapi.model.dto.responsedto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScoreResultResponseDto {

    private int coronaResult;
    private int pm10Result;
    private int pm25Result;
    //날씨
    private List<String> tempResult;
    private List<String> rainPerResult;
    private List<String> weatherResult;
    private List<String> humidityResult;
    private List<String> windResult;

    private int FineDustResult;
    private int uvResult;
    private int pollenRiskResult;
    private int coldResult;
    private int asthmaResult;
    private int foodPoisonResult;

}
