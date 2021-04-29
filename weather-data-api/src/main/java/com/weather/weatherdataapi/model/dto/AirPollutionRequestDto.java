package com.weather.weatherdataapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirPollutionRequestDto {

    private String tmX;

    private String tmY;

    private String latitude;

    private String longitude;
}
