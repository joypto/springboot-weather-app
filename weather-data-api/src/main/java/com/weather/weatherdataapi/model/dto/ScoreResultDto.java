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

    @Override
    public String toString() {
        return "ScoreResultDto{" +
                "coronaResult=" + coronaResult +
                ", pm10Result=" + pm10Result +
                ", pm25Result=" + pm25Result +
                ", tempResult=" + tempResult +
                ", rainPerResult=" + rainPerResult +
                ", weatherResult=" + weatherResult +
                ", humidityResult=" + humidityResult +
                ", windResult=" + windResult +
                ", uvResult=" + uvResult +
                ", pollenRiskResult=" + pollenRiskResult +
                ", asthmaResult=" + asthmaResult +
                ", foodPoisonResult=" + foodPoisonResult +
                '}';
    }

}
