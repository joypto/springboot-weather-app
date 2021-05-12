package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserRegionResponseDto;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final TotalDataService totalDataService;

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

    /**
     * 해당 User과 관련된 지역 정보를 반환합니다.
     *
     * @param user          특정 사용자입니다.
     * @param coordinateDto 특정 사용자의 현재 위치입니다.
     */
    public UserRegionResponseDto getMyRegion(User user, CoordinateDto coordinateDto) {
        RegionDto currentRegion = totalDataService.getRegionName(coordinateDto);

        // FIXME: 공백으로 잘라서 지역을 얻어내는 것은 위험하니까, User에서 Region을 참조하는 FK를 하나 생성해두는건 어떨까?
        String latestRequestRegionName = user.getLatestRequestRegion();
        RegionDto latestRequestRegion;

        if (StringUtils.hasText(latestRequestRegionName)) {
            // FIXME: 공백으로 나누는 것이 위험해보인다. smallRegion에는 공백으로 나누어진 문자열을 가지는 지역도 있기 때문...
            String[] regions = latestRequestRegionName.split(" ");
            latestRequestRegion = new RegionDto(regions[0], regions[1]);
        } else {
            latestRequestRegion = new RegionDto(currentRegion);
        }

        UserRegionResponseDto userRegionResponseDto = new UserRegionResponseDto();
        userRegionResponseDto.setCurrentRegion(currentRegion);
        userRegionResponseDto.setLatestRequestRegion(latestRequestRegion);
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
