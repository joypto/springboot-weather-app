package com.weather.weatherdataapi.model.dto.responsedto;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataResponseDto {

    private BigRegion bigRegion;

    private SmallRegion smallRegion;

    private LivingHealthInfo livingHealthWeather;

    private WeatherWeekInfo weekInfo;

    private WeatherDayInfo dayInfo;

    private AirPollutionInfo airPollution;

    private CoronaInfo corona;

    private List<Integer> dayScoreList;

    public WeatherDataResponseDto(BigRegion bigRegion, SmallRegion smallRegion, List<Integer> dayScoreList) {
        this.bigRegion = bigRegion;
        this.smallRegion = smallRegion;
        this.dayScoreList = dayScoreList;
    }

}
