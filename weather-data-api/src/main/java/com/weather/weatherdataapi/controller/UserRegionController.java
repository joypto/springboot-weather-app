package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserRegionResponseDto;
import com.weather.weatherdataapi.service.TotalDataService;
import com.weather.weatherdataapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
public class UserRegionController {

    private final UserService userService;
    private final TotalDataService totalDataService;

    @GetMapping("/api/user/regions")
    public UserRegionResponseDto getAllMyRegion(CoordinateDto coordinateDto, @RequestHeader("token") String token) throws ParseException {
        log.info("token='{}' \t coordinate={}", token, coordinateDto.toString());

        UserRegionResponseDto userRegionResponseDto = new UserRegionResponseDto();
        userRegionResponseDto.setCurrentRegion(totalDataService.getRegionName(coordinateDto));
        return userService.getMyRegion(userRegionResponseDto, token);
    }

    @PostMapping("/api/user/regions")
    public void saveMyRegion(@RequestBody RegionRequestDto regionRequestDto, @RequestHeader("token") String token) {
        log.info("token='{}' \t region={}", token, regionRequestDto.toString());

        userService.saveMyRegion(regionRequestDto, token);
    }

    @PutMapping("api/user/regions")
    public void updateMyRegion(@RequestBody RegionRequestDto regionRequestDto, @RequestHeader("token") String token) {
        log.info("token='{}' \t region={}", token, regionRequestDto.toString());

        userService.updateMyRegion(regionRequestDto, token);
    }

}
