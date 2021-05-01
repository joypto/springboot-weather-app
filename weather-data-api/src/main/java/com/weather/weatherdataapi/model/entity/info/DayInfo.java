package com.weather.weatherdataapi.model.entity.info;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.Region;
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
public class DayInfo {
    @JsonIgnore
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;


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

    // 강수확률
    @ElementCollection
    private List<String> dailyTime;


    // 날씨 아이콘
    @ElementCollection
    private List<String> weatherIcon;




    @Builder
    public DayInfo(List<String> tmp,List<String> weather,List<String> weatherDes, List<String> rainPer, List<String> dailyTime, List<String> weatherIcon){
        this.tmp=tmp;
        this.weather=weather;
        this.weatherDes=weatherDes;
        this.rainPer=rainPer;
        this.dailyTime = dailyTime;
        this.weatherIcon = weatherIcon;
    }
}
