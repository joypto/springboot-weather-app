package com.weather.weatherdataapi.model.dto;

import com.weather.weatherdataapi.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// 클라이언트에서 보내준 range 값
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ScoreWeightDto {

    @Getter
    private final static ScoreWeightDto defaultDto;

    static {
        defaultDto = new ScoreWeightDto(50, 50, 50, 50, 50, 0, 0, 0, 0, 0, 0, 0);
    }

    private int coronaWeight;
    private int pm10Weight;
    private int pm25Weight;
    private int tempWeight;
    private int rainPerWeight;
    private int weatherWeight;
    private int humidityWeight;
    private int windWeight;
    private int uvWeight;
    private int pollenRiskWeight;
    private int asthmaWeight;
    private int foodPoisonWeight;

    public ScoreWeightDto(User userPreference) {
        this.coronaWeight = userPreference.getCorona();
        this.pm10Weight = userPreference.getPm10();
        this.pm25Weight = userPreference.getPm25();
        this.tempWeight = userPreference.getTemp();
        this.rainPerWeight = userPreference.getRainPer();
        this.weatherWeight = userPreference.getWeather();
        this.humidityWeight = userPreference.getHumidity();
        this.windWeight = userPreference.getWind();
        this.uvWeight = userPreference.getUv();
        this.pollenRiskWeight = userPreference.getPollenRisk();
        this.asthmaWeight = userPreference.getAsthma();
        this.foodPoisonWeight = userPreference.getFoodPoison();
    }

    public int getSumOfAllWeight() {
        return this.coronaWeight
                + this.pm10Weight
                + this.pm25Weight
                + this.tempWeight
                + this.rainPerWeight
                + this.weatherWeight
                + this.humidityWeight
                + this.windWeight
                + this.uvWeight
                + this.pollenRiskWeight
                + this.asthmaWeight
                + this.foodPoisonWeight;
    }

    @Override
    public String toString() {
        return "ScoreWeightDto{" +
                "coronaWeight=" + coronaWeight +
                ", pm10Weight=" + pm10Weight +
                ", pm25Weight=" + pm25Weight +
                ", tempWeight=" + tempWeight +
                ", rainPerWeight=" + rainPerWeight +
                ", weatherWeight=" + weatherWeight +
                ", humidityWeight=" + humidityWeight +
                ", windWeight=" + windWeight +
                ", uvWeight=" + uvWeight +
                ", pollenRiskWeight=" + pollenRiskWeight +
                ", asthmaWeight=" + asthmaWeight +
                ", foodPoisonWeight=" + foodPoisonWeight +
                '}';
    }

}
