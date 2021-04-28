package com.weather.weatherdataapi.model.entity;

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
public class Corona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corona_id")
    private Long id;

    @Column
    private String sido_name;

    @Column
    private LocalDate date;

    @Column
    private Integer newLocalCaseCount;

    @Column
    private Integer newForeignCaseCount;

    public Corona(ICoronaItem item) {
        this.sido_name = item.getRegionName();
        this.date = item.getDate();
        this.newLocalCaseCount = item.getNewLocalCaseCount();
        this.newForeignCaseCount = item.getNewForeignCaseCount();
    }

}
