package com.weather.originapi.model.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Score {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private int coronaRange;

    @Column
    private int fineDustRange;

    @Column
    private int tempRange;

    @Column
    private int rainPerRange;

    @Column
    private int weatherRange;

    @Column
    private int humidityRange;

    @Column
    private int windRange;

    @Column
    private int uvRange;

    @Column
    private int pollenRiskRange;

    @Column
    private int coldRange;

}
