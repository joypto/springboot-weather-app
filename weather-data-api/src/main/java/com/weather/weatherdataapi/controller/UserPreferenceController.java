package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class UserPreferenceController {

    private final UserService userService;

    @PostMapping("/api/user/preferences")
    public User saveUserPreference(@RequestBody ScoreWeightDto scoreWeightDto, @RequestHeader(value = "token", required = false) String token) {
        log.info("token='{}' \t scoreWeight={}", token, scoreWeightDto.toString());

        User user = userService.getOrCreateUserByIdentification(token);

        userService.updatePreference(user, scoreWeightDto);

        return user;
    }

}
