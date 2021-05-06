package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.requestdto.ScoreRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.RegionResponseDto;
import com.weather.weatherdataapi.model.entity.UserPreference;
import com.weather.weatherdataapi.repository.UserPreferenceRepository;
import com.weather.weatherdataapi.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Header;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;
    private final UserPreferenceRepository userPreferenceRepository;

    @GetMapping("/api/user/preferences")
    public UserPreference getDefaultUserPreference(@RequestHeader("token") String token) {
        if (token != "") {
             return userPreferenceRepository.findByIdentification(token);
        }
        return new UserPreference("default");
    }

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

    @GetMapping("/api/user/regions")
    public RegionResponseDto getAllMyRegion(@RequestHeader("token") String token) {
        UserPreference userPreference = userPreferenceRepository.findByIdentification(token);
        return new RegionResponseDto(userPreference.getCurrentRegion(), userPreference.getSaveRegion());
    }

    @PostMapping("/api/user/regions")
    public void saveMyRegion(@RequestBody RegionRequestDto regionRequestDto, @RequestHeader("token") String token) {
        userPreferenceService.saveMyRegion(regionRequestDto, token);
    }

    @PutMapping("api/user/regions")
    public void updateMyRegion(@RequestBody RegionRequestDto regionRequestDto, @RequestHeader("token") String token) {
        userPreferenceService.updateMyRegion(regionRequestDto, token);
    }

}
