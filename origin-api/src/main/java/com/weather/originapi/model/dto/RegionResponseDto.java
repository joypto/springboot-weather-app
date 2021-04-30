package com.weather.originapi.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

// api를 호출하면 현재 기상 상태를 담는 dto
public class RegionResponseDto {

    private String uvToday;

}
