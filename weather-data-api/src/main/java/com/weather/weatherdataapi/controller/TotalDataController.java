package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.requestdto.TotalDataRequestDto;
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
public class TotalDataController {

    private final TotalDataService totalDataService;

    @GetMapping("/api/total/data/coordinate")
    public WeatherDataResponseDto getTotalDataByCoordinate(CoordinateDto coordinateDto, @RequestHeader("token") String token) throws ParseException, IOException {
        TotalDataRequestDto totalDataRequestDto = totalDataService.getRegionName(coordinateDto);
        return totalDataService.getTotalData(totalDataRequestDto, token);
    }

    @GetMapping("/api/total/data/regionname")
    public WeatherDataResponseDto getTotalDataByRegionName(TotalDataRequestDto totalDataRequestDto, @RequestHeader("token") String token) throws IOException {
        return totalDataService.getTotalData(totalDataRequestDto, token);
    }

}
