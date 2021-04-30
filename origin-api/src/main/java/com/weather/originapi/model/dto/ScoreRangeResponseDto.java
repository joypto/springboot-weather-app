package com.weather.originapi.model.dto;

import lombok.Getter;
import lombok.Setter;

// 클라이언트에서 보내준 점수를 비중으로 변환한 점수
@Getter
@Setter
public class ScoreRangeResponseDto {

    private int coronaRangeConvert;
    private int fineDustRangeConvert;
    private int tempRangeConvert;
    private int rainPerRangeConvert;
    private int weatherRangeConvert;
    private int humidityRangeConvert;
    private int windRangeConvert;
    private int uvRangeConvert;
    private int pollenRiskRangeConvert;
    private int coldRangeConvert;

}
