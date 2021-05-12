package com.weather.weatherdataapi.model.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.dto.RegionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRegionResponseDto {

    @JsonIgnore
    private String identification;

    private RegionDto currentRegion;
    private RegionDto latestRequestRegion;
    private List<String> oftenSeenRegions;

    @Override
    public String toString() {
        return "UserRegionResponseDto{" +
                "currentRegion=" + currentRegion +
                ", latestRequestRegion=" + latestRequestRegion +
                ", oftenSeenRegions=" + oftenSeenRegions +
                '}';
    }

}
