package com.weather.weatherdataapi.model.entity.region;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id;

    @Column(name = "admcode", nullable = false)
    private Long admCode;

    @Column(name = "big_region", nullable = false)
    private String bigRegion;

    @Column(name = "small_region", nullable = false)
    private String smallRegion;

}
