package com.weather.weatherdataapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateDto {

    /**
     * 경도입니다. 경도는 지구 위 X 위치를 나타내는 값입니다.
     */
    private String longitude;

    /**
     * 위도입니다. 위도는 지구 위 Y 위치를 나타내는 값입니다.
     */
    private String latitude;

}
