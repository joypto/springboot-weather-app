package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.requestdto.ScoreRequestDto;
import com.weather.weatherdataapi.model.entity.UserPreference;
import com.weather.weatherdataapi.repository.UserPreferenceRepository;
import com.weather.weatherdataapi.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Header;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class UserPreferenceController {

    private final UserPreferenceRepository userPreferenceRepository;

    @PostMapping("/api/user/preferences")
    public UserPreference saveUserPreference(@RequestBody ScoreRequestDto scoreRequestDto, @RequestHeader("token") String token) {
        UserPreference userPreference = new UserPreference(token, scoreRequestDto);
        userPreferenceRepository.save(userPreference);
        return userPreference;
    }

    @PutMapping("/api/user/preferences")
    public UserPreference updateUserPreference(@RequestBody ScoreRequestDto scoreRequestDto, @RequestHeader("token") String token) {
        UserPreference userPreference = userPreferenceRepository.findByIdentification(token);
        userPreference.updateUserPreference(scoreRequestDto);
        userPreferenceRepository.save(userPreference);
        return userPreference;
    }
}
