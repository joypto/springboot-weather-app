package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.vo.redis.UserRedisVO;
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

    boolean isFromRedis;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @JsonIgnore
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

    public User(UserRedisVO userRedisVO) {
        this.isFromRedis = true;

        this.id = userRedisVO.getId();
        this.identification = userRedisVO.getIdentification();
        this.latestRequestRegion = userRedisVO.getLatestRequestRegion();
        this.oftenSeenRegions = userRedisVO.getOftenSeenRegions();
        this.temp = userRedisVO.getTemp();
        this.rainPer = userRedisVO.getRainPer();
        this.weather = userRedisVO.getWeather();
        this.humidity = userRedisVO.getHumidity();
        this.wind = userRedisVO.getWind();
        this.pm10 = userRedisVO.getPm10();
        this.pm25 = userRedisVO.getPm25();
        this.corona = userRedisVO.getCorona();
        this.uv = userRedisVO.getUv();
        this.pollenRisk = userRedisVO.getPollenRisk();
        this.asthma = userRedisVO.getAsthma();
        this.foodPoison = userRedisVO.getFoodPoison();
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identification='" + identification + '\'' +
                ", latestRequestRegion='" + latestRequestRegion + '\'' +
                ", oftenSeenRegions=" + oftenSeenRegions +
                ", temp=" + temp +
                ", rainPer=" + rainPer +
                ", weather=" + weather +
                ", humidity=" + humidity +
                ", wind=" + wind +
                ", pm10=" + pm10 +
                ", pm25=" + pm25 +
                ", corona=" + corona +
                ", uv=" + uv +
                ", pollenRisk=" + pollenRisk +
                ", asthma=" + asthma +
                ", foodPoison=" + foodPoison +
                '}';
    }

}
