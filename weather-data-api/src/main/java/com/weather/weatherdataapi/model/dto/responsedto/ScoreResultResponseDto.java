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

    private List<String> tempResult;
    private List<String> rainPerResult;
    private List<String> weatherResult;
    private List<String> humidityResult;
    private List<String> windResult;

    private List<Integer> uvResult;
    private List<Integer> pollenRiskResult;
    private List<Integer> asthmaResult;
    private List<Integer> foodPoisonResult;

}
