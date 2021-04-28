package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.WeatherDataRequestDto;
import com.weather.weatherdataapi.service.CoronaService;
import com.weather.weatherdataapi.service.OpenApiService;
import com.weather.weatherdataapi.util.ReverseGeoCoding;
import com.weather.weatherdataapi.util.openapi.livinghealthweather.LivingHealthWeatherApiCall;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class OpenApiController {

    final private OpenApiService openApiService;
    private final LivingHealthWeatherApiCall livingHealthWeatherApiCall;
    private final ReverseGeoCoding reverseGeoCoding;
    private final CoronaService coronaService;

    @GetMapping("/api/test")
    public void show() throws IOException, ParseException {
        coronaService.fetchAndStoreCoronaInfoUsingOpenApi();
    }

    @GetMapping("/api/weather/data")
    public ReverseGeocodingResponseDto getAllWeatherData(@RequestBody WeatherDataRequestDto weatherDataRequestDto) throws ParseException, IOException {
        String latitude = weatherDataRequestDto.getLatitude();
        String longitude = weatherDataRequestDto.getLongitude();
        ReverseGeocodingResponseDto address = reverseGeoCoding.reverseGeocoding(weatherDataRequestDto.getLongitude(), weatherDataRequestDto.getLatitude());
        openApiService.callApi(weatherDataRequestDto, address);
        livingHealthWeatherApiCall.livingHealthWeatherApiCall(address);
        return address;
    }

}
