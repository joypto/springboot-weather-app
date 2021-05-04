package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.WeatherDataResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.repository.SmallRegionRepository;
import com.weather.weatherdataapi.service.AirPollutionService;
import com.weather.weatherdataapi.service.CoronaService;
import com.weather.weatherdataapi.service.TotalDataService;
import com.weather.weatherdataapi.util.openapi.geo.kakao.KakaoGeoApi;
import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponseDocument;
import com.weather.weatherdataapi.util.openapi.geo.naver.ReverseGeoCodingApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class WeatherDataController {

    private final TotalDataService totalDataService;

    @GetMapping("/api/weather/data")
    public WeatherDataResponseDto getAllWeatherData(
            @RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude, @RequestHeader("token") String token) throws ParseException, IOException {

        CoordinateDto coordinateDto = new CoordinateDto(longitude, latitude);
        return totalDataService.getTotalData(coordinateDto, token);
    }
}
