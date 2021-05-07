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

    public ReverseGeocodingResponseDto(String smallRegion) {
        this.bigRegion = "서울특별시";
        this.smallRegion = "강남구";
        this.alias = "서울";
    }

}
