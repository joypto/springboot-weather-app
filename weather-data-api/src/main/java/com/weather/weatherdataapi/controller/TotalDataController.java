package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.Global;
import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.service.TotalDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TotalDataResponseDto> getTotalDataByCoordinate(CoordinateDto coordinateDto, @RequestHeader(value = Global.IDENTIFICATION_TEXT, required = false) String identification) throws IOException {
        log.info("identification='{}' \t coordinate={}", identification, coordinateDto.toString());

        return totalDataService.getTotalDataResponse(identification, coordinateDto);
    }

    @GetMapping("/api/total/data/regionname")
    public ResponseEntity<TotalDataResponseDto> getTotalDataByRegionName(RegionDto regionDto, @RequestHeader(value = Global.IDENTIFICATION_TEXT, required = false) String identification) throws IOException {
        log.info("identification='{}' \t region={}", identification, regionDto.toString());

        return totalDataService.getTotalDataResponse(identification, regionDto);
    }

}
