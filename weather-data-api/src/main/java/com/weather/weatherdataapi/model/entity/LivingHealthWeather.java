package com.weather.weatherdataapi.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LivingHealthWeather {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column(unique = true)
    private String siDoName;

    @Column
    private String date;

    @Column
    private String areaNo;

    @Column
    private String uvToday;

    @Column
    private String uvTomorrow;

    @Column
    private String uvTheDayAfterTomorrow;

    @Column
    private String oakPollenRiskToday;

    @Column
    private String oakPollenRiskTomorrow;

    @Column
    private String oakPollenRiskTheDayAfterTomorrow;

    @Column
    private String pinePollenRiskToday;

    @Column
    private String pinePollenRiskTomorrow;

    @Column
    private String pinePollenRiskTheDayAfterTomorrow;

    @Column
    private String coldToday;

    @Column
    private String coldTomorrow;

    @Column
    private String coldTheDayAfterTomorrow;

    @Column
    private String foodPoisonToday;

    @Column
    private String foodPoisonTomorrow;

    @Column
    private String foodPoisonTheDayAfterTomorrow;

    @Column
    private String asthmaToday;

    @Column
    private String asthmaTomorrow;

    @Column
    private String asthmaTheDayAfterTomorrow;

}