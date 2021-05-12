package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserRegionResponseDto;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* Create New User */

    public User createNewUser() {
        ScoreWeightDto defaultScoreWeightDto = ScoreWeightDto.getDefaultDto();
        RegionRequestDto regionRequestDto = RegionRequestDto.getDefaultDto();

        return createNewUser(defaultScoreWeightDto, regionRequestDto);
    }

    public User createNewUser(ScoreWeightDto scoreWeightDto) {
        return createNewUser(scoreWeightDto, RegionRequestDto.getDefaultDto());
    }

    public User createNewUser(RegionRequestDto regionRequestDto) {
        return createNewUser(ScoreWeightDto.getDefaultDto(), regionRequestDto);
    }

    public User createNewUser(ScoreWeightDto scoreWeightDto, RegionRequestDto regionRequestDto) {
        String newIdentification = "wl" + ZonedDateTime.now().toString() + UUID.randomUUID();

        User newUser = new User(newIdentification, scoreWeightDto);
        newUser.setOftenSeenRegions(regionRequestDto.getOftenSeenRegions());

        userRepository.save(newUser);

        return newUser;
    }

    public User getOrCreateUserByIdentification(String identification) {
        if (StringUtils.hasText(identification))
            return createNewUser();

        Optional<User> queriedUser = userRepository.findByIdentification(identification);

        if (queriedUser.isPresent() == false)
            return createNewUser();

        return queriedUser.get();
    }

    public HttpHeaders createHeadersWithUserIdentification(User user) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("identification", user.getIdentification());
        return responseHeaders;
    }

    public UserRegionResponseDto getMyRegion(User user) {
        // FIXME: 공백으로 나누는 것이 위험해보인다. smallRegion에는 공백으로 나누어진 문자열을 가지는 지역도 있기 때문...
        String[] regions = user.getLatestRequestRegion().split(" ");
        RegionDto regionDto = new RegionDto(regions[0], regions[1]);

        UserRegionResponseDto userRegionResponseDto = new UserRegionResponseDto();
        userRegionResponseDto.setIdentification(user.getIdentification());
        userRegionResponseDto.setLatestRequestRegion(regionDto);
        userRegionResponseDto.setOftenSeenRegions(user.getOftenSeenRegions());

        return userRegionResponseDto;
    }

    // TODO: 같은 의미를 가리키는 것이지만 용어가 다르다. preference와 scoreWeight. 둘 중에 하나로 통일하는 것이 직관적이지 않을까?
    @Transactional
    public void updatePreference(User user, ScoreWeightDto scoreWeightDto) {
        user.updateUserPreference(scoreWeightDto);
    }

    @Transactional
    public void updateCurrentRegion(User user, String currentRegion) {
        user.setLatestRequestRegion(currentRegion);
        userRepository.save(user);
    }

    @Transactional
    public void updateOftenSeenRegions(User user, RegionRequestDto regionRequestDto) {
        user.setOftenSeenRegions(regionRequestDto.getOftenSeenRegions());

        userRepository.save(user);
    }

}
