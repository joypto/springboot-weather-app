package com.weather.weatherdataapi.model.dto.responsedto;

import com.weather.weatherdataapi.model.entity.*;
import com.weather.weatherdataapi.model.entity.info.*;
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

    private LivingHealthInfo livingHealthWeather;

    private WeatherWeekInfo weekInfo;

    private WeatherDayInfo dayInfo;

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
