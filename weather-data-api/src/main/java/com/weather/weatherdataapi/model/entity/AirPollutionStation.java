package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.region.Region;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class AirPollutionStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_pollution_station_id")
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "station_name")
    private String stationName;
}
