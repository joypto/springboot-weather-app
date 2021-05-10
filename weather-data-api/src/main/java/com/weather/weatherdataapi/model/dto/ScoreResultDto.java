package com.weather.weatherdataapi.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScoreResultDto {

    private Integer coronaResult;
    private Integer pm10Result;
    private Integer pm25Result;

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
