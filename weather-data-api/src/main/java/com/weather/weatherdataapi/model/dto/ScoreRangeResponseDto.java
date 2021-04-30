package com.weather.weatherdataapi.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreRangeResponseDto {

    private int coronaRangeConvert;
    private int pm10RangeConvert;
    private int pm25RangeConvert;
    private int tempRangeConvert;
    private int rainPerRangeConvert;
    private int weatherRangeConvert;
    private int humidityRangeConvert;
    private int windRangeConvert;
    private int uvRangeConvert;
    private int pollenRiskRangeConvert;
    private int coldRangeConvert;
    private int asthmaRangeConvert;
    private int foodPoisonRangeConvert;

}
