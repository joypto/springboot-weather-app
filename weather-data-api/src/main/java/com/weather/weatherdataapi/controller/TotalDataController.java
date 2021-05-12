package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.service.RegionService;
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
    private final RegionService regionService;

    @GetMapping("/api/total/data/coordinate")
    public TotalDataResponseDto getTotalDataByCoordinate(CoordinateDto coordinateDto, @RequestHeader(value = "identification", required = false) String identification) throws ParseException, IOException {
        log.info("identification='{}' \t coordinate={}", identification, coordinateDto.toString());

        RegionDto totalDataRequestDto = regionService.getRegionName(coordinateDto);
        return totalDataService.getTotalData(totalDataRequestDto, identification);
    }

    @GetMapping("/api/total/data/regionname")
    public TotalDataResponseDto getTotalDataByRegionName(RegionDto regionDto, @RequestHeader(value = "identification", required = false) String identification) throws IOException {
        log.info("identification='{}' \t coordinate={}", identification, regionDto.toString());

        return totalDataService.getTotalData(regionDto, identification);
    }

}
