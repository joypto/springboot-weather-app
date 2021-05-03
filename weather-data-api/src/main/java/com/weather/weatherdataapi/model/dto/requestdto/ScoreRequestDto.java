package com.weather.weatherdataapi.model.dto.requestdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// 클라이언트에서 보내준 range 값
@Getter
@Setter
@Builder
public class ScoreRequestDto {

    private int coronaRange;
    private int pm10Range;
    private int pm25Range;
    private int tempRange;
    private int rainPerRange;
    private int weatherRange;
    private int humidityRange;
    private int windRange;
    private int uvRange;
    private int pollenRiskRange;
    private int asthmaRange;
    private int foodPoisonRange;

    public int getSumOfAllRange() {
        return this.coronaRange
                + this.pm10Range
                + this.pm25Range
                + this.tempRange
                + this.rainPerRange
                + this.weatherRange
                + this.humidityRange
                + this.windRange
                + this.uvRange
                + this.pollenRiskRange
                + this.asthmaRange
                + this.foodPoisonRange;
    }

}
