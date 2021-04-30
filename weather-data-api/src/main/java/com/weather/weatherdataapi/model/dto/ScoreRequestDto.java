package com.weather.weatherdataapi.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// 클라이언트에서 보내준 range 값
@Getter
@Setter
@Builder(builderMethodName = "hiddenBuilder")
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
    private int coldRange;
    private int asthmaRange;
    private int foodPoisonRange;

    public static ScoreRequestDtoBuilder builder(int temp, int rainPer, int weather, int humidity, int wind, int pm10, int pm25, int corona, int uv, int pollenRisk, int cold, int asthma, int foodPoison) {
        return hiddenBuilder()
                .tempRange(temp)
                .rainPerRange(rainPer)
                .weatherRange(weather)
                .humidityRange(humidity)
                .windRange(wind)
                .pm10Range(pm10)
                .pm25Range(pm25)
                .coronaRange(corona)
                .uvRange(uv)
                .pollenRiskRange(pollenRisk)
                .coldRange(cold)
                .asthmaRange(asthma)
                .foodPoisonRange(foodPoison);
    }

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
                + this.coldRange
                + this.asthmaRange
                + this.foodPoisonRange;
    }

}
