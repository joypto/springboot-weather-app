package com.weather.weatherdataapi.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionDto {

    /**
     * 도, 광역시, 특별시 단위 지역 이름입니다.
     */
    private String bigRegionName;

    /**
     * 시, 구 단위 지역 이름입니다.
     */
    private String smallRegionName;

    public RegionDto(String bigRegionName, String smallRegionName) {
        this.bigRegionName = bigRegionName;
        this.smallRegionName = smallRegionName;
    }

}
