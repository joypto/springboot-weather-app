package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.requestdto.ScoreRequestDto;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class UserPreferenceController {

    private final UserRepository userRepository;

    @GetMapping("/api/user/preferences")
    public User getDefaultUserPreference(@RequestHeader("token") String token) {
        if (token != "") {
             return userRepository.findByIdentification(token);
        }
        return new User("default");
    }

    @PostMapping("/api/user/preferences")
    public User saveUserPreference(@RequestBody ScoreRequestDto scoreRequestDto, @RequestHeader("token") String token) {
        User userPreference = new User(token, scoreRequestDto);
        userRepository.save(userPreference);
        return userPreference;
    }

    @PutMapping("/api/user/preferences")
    public User updateUserPreference(@RequestBody ScoreRequestDto scoreRequestDto, @RequestHeader("token") String token) {
        User userPreference = userRepository.findByIdentification(token);
        userPreference.updateUserPreference(scoreRequestDto);
        userRepository.save(userPreference);
        return userPreference;
    }

}
