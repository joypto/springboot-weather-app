package com.weather.weatherdataapi.model.entity.region;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class RegionGu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_gu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_si_id")
    private RegionSi regionSi;

    @Column
    private String name;

}
