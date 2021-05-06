package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.requestdto.ScoreRequestDto;
import com.weather.weatherdataapi.model.entity.UserPreference;
import com.weather.weatherdataapi.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreference getCurrentUserPreference(String token) {
        UserPreference userPreference = new UserPreference();
        if (token != "") {
            userPreference = userPreferenceRepository.findByIdentification(token);
        } else {
            userPreference = new UserPreference("default");
        }
        return userPreference;
    }

    public void setUserCurrentRegion(UserPreference userPreference, String currentRegion) {
        if (userPreference.getIdentification() != null) {
            userPreference.setCurrentRegion(currentRegion);
            userPreferenceRepository.save(userPreference);
        }
    }



}
