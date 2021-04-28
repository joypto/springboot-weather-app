package com.weather.weatherdataapi.model.entity.region;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class RegionSi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_si_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_do_id")
    private RegionDo regionDo;

    @JsonIgnore
    @OneToMany(mappedBy = "regionSi")
    private List<RegionGu> regionGuList;

    @Column(nullable = false)
    private String name;

}
