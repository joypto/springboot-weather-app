package com.weather.originapi.model.dto;

import lombok.Getter;
import lombok.Setter;

// 최종 외출 난이도 점수 구하기 위한 항목 이거 다 더하면 외출 난이도 점수임
@Getter
@Setter
public class ScoreResultResponseDto {

    private int coronaResult;
    private int fineDustResult;
    private int tempResult;
    private int rainPerResult;
    private int weatherResult;
    private int humidityResult;
    private int windResult;
    private int uvResult;
    private int pollenRiskResult;
    private int coldResult;

}
