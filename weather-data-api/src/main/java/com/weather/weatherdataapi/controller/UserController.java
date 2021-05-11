package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserPreferenceResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserRegionResponseDto;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.service.TotalDataService;
import com.weather.weatherdataapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final TotalDataService totalDataService;

    @ResponseBody
    @GetMapping("/api/user/preferences")
    public UserPreferenceResponseDto getUserPreference(@RequestHeader(value = "token", required = false) String token) {
        User user = userService.getOrCreateUserByIdentification(token);
        UserPreferenceResponseDto responseDto = new UserPreferenceResponseDto(user);

        return responseDto;
    }

    @ResponseBody
    @GetMapping("/api/user/regions")
    public UserRegionResponseDto getUserRegion(CoordinateDto coordinateDto, @RequestHeader(value = "token", required = false) String token) throws ParseException {
        log.info("token='{}' \t coordinate={}", token, coordinateDto.toString());

        User user = userService.getOrCreateUserByIdentification(token);

        // TODO: 서비스 코드에서 userRegionResponseDto의 값을 모두 설정한 채로 내려줬으면 하는데, 어떻게 코드를 정리하는게 좋을까?
        UserRegionResponseDto userRegionResponseDto = userService.getMyRegion(user);
        userRegionResponseDto.setCurrentRegion(totalDataService.getRegionName(coordinateDto));

        return userRegionResponseDto;
    }

    @ResponseBody
    @PostMapping("/api/user/preferences")
    public String updateUserPreference(@RequestBody ScoreWeightDto scoreWeightDto, @RequestHeader(value = "token", required = false) String token) {
        log.info("token='{}' \t scoreWeight={}", token, scoreWeightDto.toString());

        User user = userService.getOrCreateUserByIdentification(token);

        userService.updatePreference(user, scoreWeightDto);

        return user.getIdentification();
    }

    @ResponseBody
    @PostMapping("/api/user/regions")
    public String updateUserRegion(@RequestBody RegionRequestDto regionRequestDto, @RequestHeader(value = "token", required = false) String token) {
        log.info("token='{}' \t region={}", token, regionRequestDto.toString());

        User user = userService.getOrCreateUserByIdentification(token);

        userService.updateOftenSeenRegions(user, regionRequestDto);

        return user.getIdentification();
    }

}
