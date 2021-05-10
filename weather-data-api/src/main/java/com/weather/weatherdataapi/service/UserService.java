package com.weather.weatherdataapi.service;

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
            userPreference = userRepository.findByIdentification(token);
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
            User user = userRepository.findByIdentification(token);
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
        if (userRepository.findByIdentification(token) == null) {
            user = new User("default");
            user.setIdentification(token);
            user.setOftenSeenRegions(regionRequestDto.getRegion());
        } else {
            user = userRepository.findByIdentification(token);
            try {
                List<String> oftenSeenRegions = user.getOftenSeenRegions();
                oftenSeenRegions.addAll(regionRequestDto.getRegion());
            } catch (NullPointerException e) {
                List<String> oftenSeenRegions = new ArrayList<>();
                oftenSeenRegions.addAll(regionRequestDto.getRegion());
            }
        }
        userRepository.save(user);
    }

    public void updateMyRegion(RegionRequestDto regionRequestDto, String token) {
        User userPreference = userRepository.findByIdentification(token);
        List<String> saveRegion = userPreference.getOftenSeenRegions();
        for (int i = 0; i <regionRequestDto.getRegion().size(); i++) {
            String region = regionRequestDto.getRegion().get(i);
            saveRegion.remove(region);
        }
        userRepository.save(userPreference);
    }

}
