package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.WeatherDataRequestDto;
import com.weather.weatherdataapi.service.CoronaService;
import com.weather.weatherdataapi.service.OpenApiService;
import com.weather.weatherdataapi.util.ReverseGeoCoding;
import com.weather.weatherdataapi.util.openapi.livinghealthweather.HealthWeatherApiCall;
import com.weather.weatherdataapi.util.openapi.livinghealthweather.LivingWeatherApiCall;
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
    private final HealthWeatherApiCall healthWeatherApiCall;
    private final LivingWeatherApiCall livingWeatherApiCall;
    private final ReverseGeoCoding reverseGeoCoding;
    private final CoronaService coronaService;

    @GetMapping("/api/test")
    public void show() throws IOException, ParseException {
        coronaService.fetchAndStoreCoronaInfoUsingOpenApi();
    }

    @GetMapping("/api/weather/data")
    public List<String> getAllWeatherData(@RequestBody WeatherDataRequestDto weatherDataRequestDto) throws ParseException, IOException {
        String latitude = weatherDataRequestDto.getLatitude();
        String longitude = weatherDataRequestDto.getLongitude();
        List<String> address = reverseGeoCoding.reverseGeocoding(weatherDataRequestDto.getLongitude(), weatherDataRequestDto.getLatitude());
        openApiService.callApi(weatherDataRequestDto);
        healthWeatherApiCall.healthWeatherApiCall(address);
//        livingWeatherApiCall.livingWeatherApiCall(address);
        return reverseGeoCoding.reverseGeocoding(weatherDataRequestDto.getLongitude(), weatherDataRequestDto.getLatitude());
    }

}
