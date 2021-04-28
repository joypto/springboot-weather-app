package com.weather.weatherdataapi.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReverseGeocodingResponseDto {

    private String bigRegion;
    private String smallRegion;
    private String alias;

}
