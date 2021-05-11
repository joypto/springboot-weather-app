package com.weather.weatherdataapi.model.dto.responsedto;

import com.weather.weatherdataapi.model.dto.RegionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRegionResponseDto {

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
