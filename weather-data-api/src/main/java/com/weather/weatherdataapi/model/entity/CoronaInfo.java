package com.weather.weatherdataapi.model.entity;

import com.weather.weatherdataapi.util.RegionUtil;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CoronaInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corona_id")
    private Long id;

    @Column(name = "big_region")
    private String bigRegion;

    @Column
    private LocalDate date;

    @Column
    private Integer newLocalCaseCount;

    @Column
    private Integer newForeignCaseCount;

    public CoronaInfo(ICoronaItem item) {
        String convertedFullName = RegionUtil.convertAliasToFullName(item.getRegionName());
        this.bigRegion = convertedFullName != null ? convertedFullName : item.getRegionName();
        
        this.date = item.getDate();
        this.newLocalCaseCount = item.getNewLocalCaseCount();
        this.newForeignCaseCount = item.getNewForeignCaseCount();
    }

}