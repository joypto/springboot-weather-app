package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

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

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "latitude", nullable = false)
    private String latitude;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "living_health_wth_id")
    private LivingHealthWeather livingHealthWeather;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "week_data_id")
    private WeekInfo weekInfo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "day_data_id")
    private DayInfo dayInfo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "air_pollution_id")
    private AirPollution airPollution;

    public void updateLivingHealthWeather(LivingHealthWeather livingHealthWeather) {
        this.livingHealthWeather = livingHealthWeather;
    }

    public void updateWeekInfo(WeekInfo weekInfo) {
        this.weekInfo = weekInfo;
    }


    public void updateDayInfo(DayInfo dayInfo) {
        this.dayInfo = dayInfo;
    }

    public void updateAirPollution(AirPollution airPollution) {
        this.airPollution = airPollution;
    }
}