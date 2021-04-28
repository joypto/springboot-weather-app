package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OpenApiController {

    final private OpenApiService openApiService;
    private final ReverseGeoCoding reverseGeoCoding;

    @GetMapping("api/test")
    public void show(){
        openApiService.callApi();
    }
  
  @GetMapping("/api/weather/data")
    public String getAllWeatherData(@RequestBody WeatherDataRequestDto weatherDataRequestDto) throws ParseException {
        return reverseGeoCoding.reverseGeocoding(weatherDataRequestDto.getLongitude(), weatherDataRequestDto.getLatitude());
    }

}
