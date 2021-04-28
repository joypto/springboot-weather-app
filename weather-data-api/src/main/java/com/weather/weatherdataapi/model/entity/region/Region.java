package com.weather.weatherdataapi.model.entity.region;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.LivingHealthWeather;
import com.weather.weatherdataapi.model.entity.WeekInfo;
import com.weather.weatherdataapi.repository.LivingHealthWeatherRepository;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    @JsonIgnore
    private Long id;

    @Column(name = "admcode", nullable = false)
    @JsonIgnore
    private Long admCode;

    @Column(name = "big_region", nullable = false)
    private String bigRegion;

    @Column(name = "small_region", nullable = false)
    private String smallRegion;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "living_health_wth_id")
    private LivingHealthWeather livingHealthWeather;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "week_data_id")
    private WeekInfo weekInfo;

    public void updateLivingHealthWeather(LivingHealthWeather livingHealthWeather) {
        this.livingHealthWeather = livingHealthWeather;
    }

    public void updateWeekInfo(WeekInfo weekInfo){
        this.weekInfo = weekInfo;
    }
}
