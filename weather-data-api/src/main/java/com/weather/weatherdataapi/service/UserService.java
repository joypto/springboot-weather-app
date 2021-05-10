package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.exception.repository.InvalidUserException;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserRegionResponseDto;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUserPreference(String token) {
        User userPreference = new User();
        if (StringUtils.hasText(token) == true) {
            userPreference = userRepository.findByIdentification(token).orElseThrow(() -> new InvalidUserException());
        } else {
            userPreference = new User("default");
        }
        return userPreference;
    }

    public void setUserCurrentRegion(User userPreference, String currentRegion) {
        if (userPreference.getIdentification() != null) {
            userPreference.setLatestRequestRegion(currentRegion);
            userRepository.save(userPreference);
        }
    }

    public UserRegionResponseDto getMyRegion(UserRegionResponseDto userRegionResponseDto, String token) {
        try {
            User user = userRepository.findByIdentification(token).orElseThrow(() -> new InvalidUserException());
            String[] regions = user.getLatestRequestRegion().split(" ");
            RegionDto regionDto = new RegionDto(regions[0], regions[1]);
            userRegionResponseDto.setLatestRequestRegion(regionDto);

            userRegionResponseDto.setOftenSeenRegions(user.getOftenSeenRegions());

            return userRegionResponseDto;
        } catch (NullPointerException e) {
            return userRegionResponseDto;
        }
    }

    public void saveMyRegion(RegionRequestDto regionRequestDto, String token) {
        User user;
        if (userRepository.findByIdentification(token).isPresent()) {
            user = new User("default");
            user.setIdentification(token);
            List<String> oftenSeenRegions = new ArrayList<>();
            oftenSeenRegions.add(regionRequestDto.getRegion());
            user.setOftenSeenRegions(oftenSeenRegions);
        } else {
            user = userRepository.findByIdentification(token).orElseThrow(() -> new InvalidUserException());
            try {
                List<String> oftenSeenRegions = user.getOftenSeenRegions();
                oftenSeenRegions.add(regionRequestDto.getRegion());
            } catch (NullPointerException e) {
                List<String> oftenSeenRegions = new ArrayList<>();
                oftenSeenRegions.add(regionRequestDto.getRegion());
                user.setOftenSeenRegions(oftenSeenRegions);
            }
        }
        userRepository.save(user);
    }

    public void updateMyRegion(RegionRequestDto regionRequestDto, String token) {
        User userPreference = userRepository.findByIdentification(token).orElseThrow(() -> new InvalidUserException());
        List<String> saveRegion = userPreference.getOftenSeenRegions();
        saveRegion.remove(regionRequestDto.getRegion());
        userRepository.save(userPreference);
    }

}
