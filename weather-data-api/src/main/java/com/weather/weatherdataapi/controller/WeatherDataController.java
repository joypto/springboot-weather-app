package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.responsedto.WeatherDataResponseDto;
import com.weather.weatherdataapi.service.TotalDataService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class WeatherDataController {

    private final TotalDataService totalDataService;

    @GetMapping("/api/weather/data")
    public WeatherDataResponseDto getAllWeatherData(CoordinateDto coordinateDto, @RequestHeader("token") String token) throws ParseException, IOException {
        return totalDataService.getTotalData(coordinateDto, token);
    }
}
