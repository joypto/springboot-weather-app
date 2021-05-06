package com.weather.weatherdataapi.model.dto.requestdto;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalDataRequestDto {

    private String currentBigRegionName;
    private String currentSmallRegionName;

    public TotalDataRequestDto(String currentBigRegionName, String currentSmallRegionName) {
        this.currentBigRegionName = currentBigRegionName;
        this.currentSmallRegionName = currentSmallRegionName;
    }

}
