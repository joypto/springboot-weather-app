package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.requestdto.ScoreRequestDto;
import com.weather.weatherdataapi.model.entity.UserPreference;
import com.weather.weatherdataapi.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void saveMyRegion(RegionRequestDto regionRequestDto, String token) {
        UserPreference userPreference = userPreferenceRepository.findByIdentification(token);
        List<String> saveRegion = userPreference.getSaveRegion();
        saveRegion.add(regionRequestDto.getRegion());
        userPreferenceRepository.save(userPreference);
    }

    public void updateMyRegion(RegionRequestDto regionRequestDto, String token) {
        UserPreference userPreference = userPreferenceRepository.findByIdentification(token);
        List<String> saveRegion = userPreference.getSaveRegion();
        saveRegion.remove(regionRequestDto.getRegion());
        userPreferenceRepository.save(userPreference);
    }

}
