package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.responsedto.WeatherDataResponseDto;
import com.weather.weatherdataapi.model.entity.info.WeatherWeekInfo;
import com.weather.weatherdataapi.model.vo.redis.WeatherWeekRedisVO;
import com.weather.weatherdataapi.repository.info.WeatherWeekInfoRepository;
import com.weather.weatherdataapi.repository.redis.WeatherWeekRedisRepository;
import com.weather.weatherdataapi.service.TotalDataService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

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
