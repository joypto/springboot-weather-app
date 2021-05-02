package com.weather.weatherdataapi.model.entity.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class WeatherWeekInfo extends Timestamped {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "small_region_id")
    private SmallRegion smallRegion;

    @ElementCollection
    private List<String> maxTmp;
    // 최고 기온
    @ElementCollection
    private List<String> minTmp;
    // 기온
    @ElementCollection
    private List<String> tmp;
    // 습도
    @ElementCollection
    private List<String> humidity;
    // 날씨
    @ElementCollection
    private List<String> weather;
    // 날씨 설명
    @ElementCollection
    private List<String> weatherDes;
    // 강수확률
    @ElementCollection
    private List<String> rainPer;

    // 바람
    @ElementCollection
    private List<String> windSpeed;

    // 날씨 아이콘
    @ElementCollection
    private List<String> weatherIcon;

    // 강수량
    @JsonIgnore
    @ElementCollection
    private List<String> rain;


    @Builder
    public WeatherWeekInfo(List<String> weatherIcon, List<String> maxTmp, List<String> minTmp, List<String> tmp, List<String> humidity, List<String> weather, List<String> weatherDes, List<String> rainPer, List<String> rain, List<String> windSpeed) {
        this.maxTmp = maxTmp;
        this.minTmp = minTmp;
        this.tmp = tmp;
        this.humidity = humidity;
        this.weather = weather;
        this.weatherDes = weatherDes;
        this.rainPer = rainPer;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.weatherIcon = weatherIcon;
    }
}
