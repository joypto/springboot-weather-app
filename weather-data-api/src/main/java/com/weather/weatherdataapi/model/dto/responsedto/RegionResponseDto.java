package com.weather.weatherdataapi.model.dto.responsedto;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegionResponseDto {

    private String currentRegion;
    private List<String> saveRegions;

    public RegionResponseDto(String currentRegion, List<String> saveRegions) {
        this.currentRegion = currentRegion;
        this.saveRegions = saveRegions;
    }

}
