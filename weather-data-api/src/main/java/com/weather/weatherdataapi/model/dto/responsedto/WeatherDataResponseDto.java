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

    private int coronaTotalNewCaseCount;

    private List<Integer> dayScoreList;

    public WeatherDataResponseDto(BigRegion bigRegion, SmallRegion smallRegion, CoronaInfo corona, int coronaTotalNewCaseCount, List<Integer> dayScoreList) {
        this.bigRegion = bigRegion;
        this.smallRegion = smallRegion;
        this.corona = corona;
        this.coronaTotalNewCaseCount = coronaTotalNewCaseCount;
        this.dayScoreList = dayScoreList;

        this.livingHealthWeather = bigRegion.getLivingHealthInfoList().get(0);
        this.weekInfo = smallRegion.getWeatherWeekInfoList().get(0);
        this.dayInfo = smallRegion.getWeatherDayInfoList().get(0);
        this.airPollution = smallRegion.getAirPollutionInfoList().get(0);
    }

}
