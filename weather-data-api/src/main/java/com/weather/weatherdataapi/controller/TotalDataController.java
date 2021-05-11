package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.service.TotalDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@RestController
public class TotalDataController {

    private final TotalDataService totalDataService;

    @GetMapping("/api/total/data/coordinate")
    public TotalDataResponseDto getTotalDataByCoordinate(CoordinateDto coordinateDto, @RequestHeader("token") String token) throws ParseException, IOException {
        log.info("token='{}' \t coordinate={}", token, coordinateDto.toString());

        RegionDto totalDataRequestDto = totalDataService.getRegionName(coordinateDto);
        return totalDataService.getTotalData(totalDataRequestDto, token);
    }

    @GetMapping("/api/total/data/regionname")
    public TotalDataResponseDto getTotalDataByRegionName(RegionDto regionDto, @RequestHeader("token") String token) throws IOException {
        log.info("token='{}' \t coordinate={}", token, regionDto.toString());

        return totalDataService.getTotalData(regionDto, token);
    }

}
