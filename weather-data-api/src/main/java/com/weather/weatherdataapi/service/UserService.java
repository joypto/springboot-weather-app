package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.exception.repository.InvalidUserException;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserRegionResponseDto;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Total Data를 조회할 때 필요한 값 세팅입니다.
     */
    public User getCurrentUserPreference(String token) {
        User user = new User();
        if (StringUtils.hasText(token)) {
            try {
                user = userRepository.findByIdentification(token).orElseThrow(() -> new InvalidUserException());
            } catch (InvalidUserException e) {
                user = new User("default");
            }
        } else {
            user = new User("default");
        }
        return user;
    }

    public void setUserCurrentRegion(User user, String currentRegion) {
        if (StringUtils.hasText(user.getIdentification())) {
            user.setLatestRequestRegion(currentRegion);
            userRepository.save(user);
        }
    }

    /**
     * 유저가 설정한 지역 관련 메서드들입니다.
     */
    public UserRegionResponseDto getMyRegion(UserRegionResponseDto userRegionResponseDto, String token) {
        try {
            User user = userRepository.findByIdentification(token).orElseThrow(() -> new InvalidUserException());
            String[] regions = user.getLatestRequestRegion().split(" ");
            RegionDto regionDto = new RegionDto(regions[0], regions[1]);
            userRegionResponseDto.setLatestRequestRegion(regionDto);

            userRegionResponseDto.setOftenSeenRegions(user.getOftenSeenRegions());

            return userRegionResponseDto;
        } catch (NullPointerException | InvalidUserException e) {
            return userRegionResponseDto;
        }
    }

    public void updateMyRegion(RegionRequestDto regionRequestDto, String token) {
        User user = userRepository.findByIdentification(token).orElseThrow(() -> new InvalidUserException());

        user.setOftenSeenRegions(regionRequestDto.getRegionList());

        userRepository.save(user);
    }

}
