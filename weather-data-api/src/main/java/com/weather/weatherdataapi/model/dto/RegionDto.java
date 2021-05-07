package com.weather.weatherdataapi.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionDto {

    private String bigRegionName;
    private String smallRegionName;

    public RegionDto(String currentBigRegionName, String currentSmallRegionName) {
        this.bigRegionName = currentBigRegionName;
        this.smallRegionName = currentSmallRegionName;
    }

}
