package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.region.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LivingHealthWeather {

    @JsonIgnore
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;

    @JsonIgnore
    @Column
    private String date;

    @JsonIgnore
    @Column
    private String areaNo;

    @Column
    private String uvToday;

    @JsonIgnore
    @Column
    private String uvTomorrow;

    @JsonIgnore
    @Column
    private String uvTheDayAfterTomorrow;

    @Column
    private String oakPollenRiskToday;

    @JsonIgnore
    @Column
    private String oakPollenRiskTomorrow;

    @JsonIgnore
    @Column
    private String oakPollenRiskTheDayAfterTomorrow;

    @Column
    private String pinePollenRiskToday;

    @JsonIgnore
    @Column
    private String pinePollenRiskTomorrow;

    @JsonIgnore
    @Column
    private String pinePollenRiskTheDayAfterTomorrow;

    @Column
    private String coldToday;

    @JsonIgnore
    @Column
    private String coldTomorrow;

    @JsonIgnore
    @Column
    private String coldTheDayAfterTomorrow;

    @Column
    private String foodPoisonToday;

    @JsonIgnore
    @Column
    private String foodPoisonTomorrow;

    @JsonIgnore
    @Column
    private String foodPoisonTheDayAfterTomorrow;

    @Column
    private String asthmaToday;

    @JsonIgnore
    @Column
    private String asthmaTomorrow;

    @JsonIgnore
    @Column
    private String asthmaTheDayAfterTomorrow;

}
