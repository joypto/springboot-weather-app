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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final TotalDataService totalDataService;

    @GetMapping("/api/user/preferences")
    public UserPreferenceResponseDto getUserPreference(@RequestHeader(value = "identification", required = false) String identification) {
        log.info("identification='{}'", identification);

        User user = userService.getOrCreateUserByIdentification(identification);
        UserPreferenceResponseDto responseDto = new UserPreferenceResponseDto(user);

        return responseDto;
    }

    @GetMapping("/api/user/regions")
    public UserRegionResponseDto getUserRegion(CoordinateDto coordinateDto, @RequestHeader(value = "identification", required = false) String identification) throws ParseException {
        log.info("identification='{}' \t coordinate={}", identification, coordinateDto.toString());

        User user = userService.getOrCreateUserByIdentification(identification);

        UserRegionResponseDto userRegionResponseDto = userService.getMyRegion(user, coordinateDto);

        return userRegionResponseDto;
    }

    @PostMapping("/api/user/preferences")
    public String updateUserPreference(@RequestBody ScoreWeightDto scoreWeightDto, @RequestHeader(value = "identification", required = false) String identification) {
        log.info("identification='{}' \t scoreWeight={}", identification, scoreWeightDto.toString());

        User user = userService.getOrCreateUserByIdentification(identification);

        userService.updatePreference(user, scoreWeightDto);

        return user.getIdentification();
    }

    @PostMapping("/api/user/regions")
    public String updateMyRegion(@RequestBody RegionRequestDto regionRequestDto, @RequestHeader(value = "identification", required = false) String identification) {
        log.info("identification='{}' \t region={}", identification, regionRequestDto.toString());

        User user = userService.getOrCreateUserByIdentification(identification);

        userService.updateOftenSeenRegions(user, regionRequestDto);

        return user.getIdentification();
    }

}
