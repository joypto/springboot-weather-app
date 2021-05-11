package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserRegionResponseDto;
import com.weather.weatherdataapi.model.entity.User;
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
    public UserRegionResponseDto getAllMyRegion(CoordinateDto coordinateDto, @RequestHeader(value = "token", required = false) String token) throws ParseException {
        log.info("token='{}' \t coordinate={}", token, coordinateDto.toString());

        User user = userService.getOrCreateUserByIdentification(token);

        // TODO: 서비스 코드에서 userRegionResponseDto의 값을 모두 설정한 채로 내려줬으면 하는데, 어떻게 코드를 정리하는게 좋을까?
        UserRegionResponseDto userRegionResponseDto = userService.getMyRegion(user);
        userRegionResponseDto.setCurrentRegion(totalDataService.getRegionName(coordinateDto));

        return userRegionResponseDto;
    }

    @PostMapping("/api/user/regions")
    public User updateMyRegion(@RequestBody RegionRequestDto regionRequestDto, @RequestHeader(value = "token", required = false) String token) {
        log.info("token='{}' \t region={}", token, regionRequestDto.toString());

        User user = userService.getOrCreateUserByIdentification(token);

        userService.updateOftenSeenRegions(user, regionRequestDto);

        return user;
    }

}
