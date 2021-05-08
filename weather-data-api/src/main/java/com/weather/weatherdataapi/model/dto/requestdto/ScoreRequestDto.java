package com.weather.weatherdataapi.model.dto.requestdto;

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

    public ScoreRequestDto(User userPreference) {
        this.coronaRange = userPreference.getCorona();
        this.pm10Range = userPreference.getPm10();
        this.pm25Range = userPreference.getPm25();
        this.tempRange = userPreference.getTemp();
        this.rainPerRange = userPreference.getRainPer();
        this.weatherRange = userPreference.getWeather();
        this.humidityRange = userPreference.getHumidity();
        this.windRange = userPreference.getWind();
        this.uvRange = userPreference.getUv();
        this.pollenRiskRange = userPreference.getPollenRisk();
        this.asthmaRange = userPreference.getAsthma();
        this.foodPoisonRange = userPreference.getFoodPoison();
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
                + this.asthmaRange
                + this.foodPoisonRange;
    }

}
