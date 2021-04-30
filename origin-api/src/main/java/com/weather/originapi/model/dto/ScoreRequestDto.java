package com.weather.originapi.model.dto;

import lombok.Getter;
import lombok.Setter;

// 클라이언트에서 보내준 range 값
@Getter
@Setter
public class ScoreRequestDto {

    private int coronaRange;
    private int fineDustRange;
    private int tempRange;
    private int rainPerRange;
    private int weatherRange;
    private int humidityRange;
    private int windRange;
    private int uvRange;
    private int pollenRiskRange;
    private int coldRange;

}
