package com.weather.weatherdataapi.model.dto.responsedto.info;

import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LivingHealthResponseDto {

//    private final LocalDate date;

    private final String uvToday;

    private final String uvTomorrow;

    private final String uvTheDayAfterTomorrow;

    private final String oakPollenRiskToday;

    private final String oakPollenRiskTomorrow;

    private final String oakPollenRiskTheDayAfterTomorrow;

    private final String foodPoisonToday;

    private final String foodPoisonTomorrow;

    private final String foodPoisonTheDayAfterTomorrow;

    private final String asthmaToday;

    private final String asthmaTomorrow;

    private final String asthmaTheDayAfterTomorrow;

    public LivingHealthResponseDto(LivingHealthInfo info) {
        this.uvToday = info.getUvToday().toString();
        this.uvTomorrow = info.getUvTomorrow().toString();
        this.uvTheDayAfterTomorrow = info.getUvTheDayAfterTomorrow().toString();
        this.oakPollenRiskToday = info.getOakPollenRiskToday().toString();
        this.oakPollenRiskTomorrow = info.getOakPollenRiskTomorrow().toString();
        this.oakPollenRiskTheDayAfterTomorrow = info.getAsthmaTheDayAfterTomorrow().toString();
        this.foodPoisonToday = info.getFoodPoisonToday().toString();
        this.foodPoisonTomorrow = info.getFoodPoisonTomorrow().toString();
        this.foodPoisonTheDayAfterTomorrow = info.getFoodPoisonTheDayAfterTomorrow().toString();
        this.asthmaToday = info.getAsthmaToday().toString();
        this.asthmaTomorrow = info.getAsthmaTomorrow().toString();
        this.asthmaTheDayAfterTomorrow = info.getAsthmaTheDayAfterTomorrow().toString();
    }

}
