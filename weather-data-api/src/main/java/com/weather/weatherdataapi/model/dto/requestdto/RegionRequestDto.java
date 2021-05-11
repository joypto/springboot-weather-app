package com.weather.weatherdataapi.model.dto.requestdto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegionRequestDto {

    @Getter
    private final static RegionRequestDto defaultDto;

    static {
        defaultDto = new RegionRequestDto();
    }

    private List<String> regionList;

    @Override
    public String toString() {
        return "RegionRequestDto{" +
                "region=" + regionList +
                '}';
    }
}
