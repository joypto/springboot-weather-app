package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BigRegion {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "big_region_id")
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "bigRegion")
    private List<SmallRegion> smallRegionList;

    @Column(name = "big_region_name")
    private String bigRegionName;

    @Column(name = "adm_code")
    private String admCode;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

}
