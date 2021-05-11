package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_preference_id")
    private Long id;

    @Column
    private String identification;

    @JsonIgnore
    @Column
    private String latestRequestRegion;

    @JsonIgnore
    @ElementCollection
    private List<String> oftenSeenRegions;

    @Column
    private int temp;

    @Column
    private int rainPer;

    @Column
    private int weather;

    @Column
    private int humidity;

    @Column
    private int wind;

    @Column
    private int pm10;

    @Column
    private int pm25;

    @Column
    private int corona;

    @Column
    private int uv;

    @Column
    private int pollenRisk;

    @Column
    private int asthma;

    @Column
    private int foodPoison;

    public User(String identification, ScoreWeightDto scoreWeightDto) {
        this.identification = identification;
        this.temp = scoreWeightDto.getTempWeight();
        this.rainPer = scoreWeightDto.getRainPerWeight();
        this.weather = scoreWeightDto.getWeatherWeight();
        this.humidity = scoreWeightDto.getHumidityWeight();
        this.wind = scoreWeightDto.getWindWeight();
        this.pm10 = scoreWeightDto.getPm10Weight();
        this.pm25 = scoreWeightDto.getPm25Weight();
        this.corona = scoreWeightDto.getCoronaWeight();
        this.uv = scoreWeightDto.getUvWeight();
        this.pollenRisk = scoreWeightDto.getPollenRiskWeight();
        this.asthma = scoreWeightDto.getAsthmaWeight();
        this.foodPoison = scoreWeightDto.getFoodPoisonWeight();
    }

    public User(String identification) {
        this.temp = 50;
        this.rainPer = 50;
        this.weather = 0;
        this.humidity = 0;
        this.wind = 0;
        this.pm10 = 50;
        this.pm25 = 50;
        this.corona = 50;
        this.uv = 0;
        this.pollenRisk = 0;
        this.asthma = 0;
        this.foodPoison = 0;
    }

    public void updateUserPreference(ScoreWeightDto scoreWeightDto) {
        this.temp = scoreWeightDto.getTempWeight();
        this.rainPer = scoreWeightDto.getRainPerWeight();
        this.weather = scoreWeightDto.getWeatherWeight();
        this.humidity = scoreWeightDto.getHumidityWeight();
        this.wind = scoreWeightDto.getWindWeight();
        this.pm10 = scoreWeightDto.getPm10Weight();
        this.pm25 = scoreWeightDto.getPm25Weight();
        this.corona = scoreWeightDto.getCoronaWeight();
        this.uv = scoreWeightDto.getUvWeight();
        this.pollenRisk = scoreWeightDto.getPollenRiskWeight();
        this.asthma = scoreWeightDto.getAsthmaWeight();
        this.foodPoison = scoreWeightDto.getFoodPoisonWeight();
    }

}
