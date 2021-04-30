package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class WeekInfo extends Timestamped{

    @JsonIgnore
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    // 최저 기온
    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;

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
    // 강수량
    @JsonIgnore
    @ElementCollection
    private List<String> rain;

    @Builder
    public WeekInfo(List<String> maxTmp,List<String> minTmp,List<String> tmp,List<String> humidity,List<String> weather,List<String> weatherDes,List<String> rainPer,List<String> rain){
        this.maxTmp = maxTmp;
        this.minTmp = minTmp;
        this.tmp = tmp;
        this.humidity = humidity;
        this.weather = weather;
        this.weatherDes = weatherDes;
        this.rainPer = rainPer;
        this.rain = rain;
    }
}
