package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.WeatherDataRequestDto;
import com.weather.weatherdataapi.util.ReverseGeoCoding;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenApiController {

    private final ReverseGeoCoding reverseGeoCoding;

    @GetMapping("/api/weather/data")
    public String getAllWeatherData(@RequestBody WeatherDataRequestDto weatherDataRequestDto) throws ParseException {
        return reverseGeoCoding.reverseGeocoding(weatherDataRequestDto.getLongitude(), weatherDataRequestDto.getLatitude());
    }

}
