package com.weather.weatherdataapi.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class DayInfo {
    @JsonIgnore
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    // 기온
    @ElementCollection
    private List<String> tmp;
    // 날씨
    @ElementCollection
    private List<String> weather;
    // 날씨 설명
    @ElementCollection
    private List<String> weatherDes;
    // 강수확률
    @ElementCollection
    private List<String> rainPer;

    @Builder
    public DayInfo(List<String> tmp,List<String> weather,List<String> weatherDes, List<String> rainPer){
        this.tmp=tmp;
        this.weather=weather;
        this.weatherDes=weatherDes;
        this.rainPer=rainPer;
    }
}
