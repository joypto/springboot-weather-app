package com.weather.weatherdataapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

    public RegionDto(RegionDto other) {
        this.bigRegionName = other.getBigRegionName();
        this.smallRegionName = other.getSmallRegionName();
    }

    @Override
    public String toString() {
        return "RegionDto{" + "bigRegionName='" + bigRegionName + "', smallRegionName='" + smallRegionName + "'}";
    }
}
