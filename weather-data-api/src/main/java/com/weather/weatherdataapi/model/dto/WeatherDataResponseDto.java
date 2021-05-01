package com.weather.weatherdataapi.model.dto;

import com.weather.weatherdataapi.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataResponseDto {

    private Region region;

    private LivingHealthWeather livingHealthWeather;

    private WeekInfo weekInfo;

    private DayInfo dayInfo;

    private AirPollutionInfo airPollution;

    private CoronaInfo corona;

    private CoronaInfo coronaTotal;

    private int score;

    public WeatherDataResponseDto(Region region, CoronaInfo corona, CoronaInfo coronaTotal, int score) {
        this.region = region;
        this.livingHealthWeather = region.getLivingHealthWeather();
        this.weekInfo = region.getWeekInfo();
        this.dayInfo = region.getDayInfo();
        this.airPollution = region.getAirPollution();
        this.corona = corona;
        this.coronaTotal = coronaTotal;
        this.score = score;
    }

}
