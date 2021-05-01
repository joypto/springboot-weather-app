package com.weather.weatherdataapi.model.dto.responsedto;

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
