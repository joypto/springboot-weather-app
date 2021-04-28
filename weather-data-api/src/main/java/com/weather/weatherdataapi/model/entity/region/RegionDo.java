package com.weather.weatherdataapi.model.entity.region;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class RegionDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_do_id")
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "regionDo", fetch = FetchType.LAZY)
    private List<RegionSi> regionSiList;

    @Column()
    private String name;

}
