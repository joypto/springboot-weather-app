package com.weather.weatherdataapi.model.entity.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LivingHealthInfo {

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
