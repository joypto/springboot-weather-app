package com.weather.weatherdataapi.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UVIdx {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    private String date;

    @Column
    private String areaNo;

    @Column
    private String today;

    @Column
    private String tomorrow;

    @Column
    private String theDayAfterTomorrow;

}
