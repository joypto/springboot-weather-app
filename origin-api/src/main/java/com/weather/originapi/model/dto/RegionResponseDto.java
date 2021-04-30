package com.weather.originapi.model.dto;

import lombok.Getter;
import lombok.Setter;

// api를 호출하면 현재 기상 상태를 담는 dto
@Getter
@Setter
public class RegionResponseDto {

    private Object region;
}
