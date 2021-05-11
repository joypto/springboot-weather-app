package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.exception.repository.InvalidUserException;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class UserPreferenceController {

    private final UserRepository userRepository;

    @GetMapping("/api/user/preferences")
    public User getDefaultUserPreference(@RequestHeader("token") String token) {
        if (StringUtils.hasText(token)) {
            System.out.println("token 값: " + token);
            try {
                return userRepository.findByIdentification(token).orElseThrow(() -> new InvalidUserException());
            } catch (InvalidUserException e) {
                return new User("default");
            }
        }
        return new User("default");
    }

    @PostMapping("/api/user/preferences")
    public User saveUserPreference(@RequestBody ScoreWeightDto scoreWeightDto, @RequestHeader("token") String token) {
        User userPreference = new User(token, scoreWeightDto);
        userRepository.save(userPreference);
        return userPreference;
    }

    @PutMapping("/api/user/preferences")
    public User updateUserPreference(@RequestBody ScoreWeightDto scoreWeightDto, @RequestHeader("token") String token) {
        User userPreference = userRepository.findByIdentification(token).orElseThrow(() -> new InvalidUserException());
        userPreference.updateUserPreference(scoreWeightDto);
        userRepository.save(userPreference);
        return userPreference;
    }

}
