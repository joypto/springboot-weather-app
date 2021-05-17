package com.weather.weatherdataapi.model.dto.requestdto;

import com.weather.weatherdataapi.model.dto.RegionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegionRequestDto {

    @Getter
    private final static RegionRequestDto defaultDto;

    static {
        List<RegionDto> emptyRegionDtoList = new ArrayList<>(0);

        defaultDto = new RegionRequestDto(emptyRegionDtoList);
    }

    private List<RegionDto> oftenSeenRegions;

    @Override
    public String toString() {
        return "RegionRequestDto{" +
                "oftenSeenRegions=" + oftenSeenRegions +
                '}';
    }
}
