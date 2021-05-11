package com.weather.weatherdataapi.model.dto.requestdto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegionRequestDto {

    private List<String> region;

    @Override
    public String toString() {
        return "RegionRequestDto{" +
                "region=" + region +
                '}';
    }
}
