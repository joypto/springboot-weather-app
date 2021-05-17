package com.weather.weatherdataapi.model.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.entity.UserOftenSeenRegion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRegionResponseDto {

    @JsonIgnore
    private String identification;

    private RegionDto currentRegion;
    private RegionDto latestRequestRegion;
    private List<RegionDto> oftenSeenRegions;

    public UserRegionResponseDto(String identification, RegionDto currentRegion, RegionDto latestRequestRegion, List<UserOftenSeenRegion> oftenSeenRegions) {
        this.identification = identification;
        this.currentRegion = currentRegion;
        this.latestRequestRegion = latestRequestRegion;

        this.oftenSeenRegions = new ArrayList<>(oftenSeenRegions.size());
        for (UserOftenSeenRegion region : oftenSeenRegions) {
            String bigRegionName = region.getSmallRegion().getBigRegion().getBigRegionName();
            String smallRegionName = region.getSmallRegion().getSmallRegionName();

            this.oftenSeenRegions.add(new RegionDto(bigRegionName, smallRegionName));
        }

    }

    @Override
    public String toString() {
        return "UserRegionResponseDto{" +
                "currentRegion=" + currentRegion +
                ", latestRequestRegion=" + latestRequestRegion +
                ", oftenSeenRegions=" + oftenSeenRegions +
                '}';
    }

}
