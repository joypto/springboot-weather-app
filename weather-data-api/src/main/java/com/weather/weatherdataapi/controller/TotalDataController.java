package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.service.TotalDataService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class TotalDataController {

    private final TotalDataService totalDataService;

    @GetMapping("/api/total/data/coordinate")
    public TotalDataResponseDto getTotalDataByCoordinate(CoordinateDto coordinateDto, @RequestHeader("token") String token) throws ParseException, IOException {
        RegionDto totalDataRequestDto = totalDataService.getRegionName(coordinateDto);
        return totalDataService.getTotalData(totalDataRequestDto, token);
    }

    @GetMapping("/api/total/data/regionname")
    public TotalDataResponseDto getTotalDataByRegionName(RegionDto totalDataRequestDto, @RequestHeader("token") String token) throws IOException {
        return totalDataService.getTotalData(totalDataRequestDto, token);
    }

}
