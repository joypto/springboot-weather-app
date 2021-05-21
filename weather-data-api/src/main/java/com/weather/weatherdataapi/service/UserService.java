package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.Global;
import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserRegionResponseDto;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.model.entity.UserDevice;
import com.weather.weatherdataapi.model.entity.UserOftenSeenRegion;
import com.weather.weatherdataapi.repository.UserDeviceRepository;
import com.weather.weatherdataapi.repository.UserOftenSeenRegionRepository;
import com.weather.weatherdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserOftenSeenRegionRepository userOftenSeenRegionRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final RegionService regionService;

    /* Create New User */

    public User createNewUser() {
        ScoreWeightDto defaultScoreWeightDto = ScoreWeightDto.getDefaultDto();

        return createNewUser(defaultScoreWeightDto);
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

        userRepository.save(newUser);

        updateOftenSeenRegionRefs(newUser, regionRequestDto);

        return newUser;
    }

    public User getOrCreateUserByIdentification(String identification) {
        if (StringUtils.hasText(identification) == false)
            return createNewUser();

        Optional<User> queriedUser = getUserByIdentification(identification);

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
        RegionDto currentRegion = regionService.getRegionName(coordinateDto);

        SmallRegion latestRequestRegion = user.getLatestRequestRegion();
        RegionDto latestRequestRegionDto = latestRequestRegion == null ? null : new RegionDto(latestRequestRegion.getBigRegion().getBigRegionName(), latestRequestRegion.getSmallRegionName());

        List<UserOftenSeenRegion> oftenSeenRegions = userOftenSeenRegionRepository.findAllByUser(user);

        UserRegionResponseDto userRegionResponseDto = new UserRegionResponseDto(user.getIdentification(), currentRegion, latestRequestRegionDto, oftenSeenRegions);

        return userRegionResponseDto;
    }

    // TODO: 같은 의미를 가리키는 것이지만 용어가 다르다. preference와 scoreWeight. 둘 중에 하나로 통일하는 것이 직관적이지 않을까?
    @Transactional
    public void updatePreference(User user, ScoreWeightDto scoreWeightDto) {
        user.updateUserPreference(scoreWeightDto);
    }

    @Transactional
    public void updateCurrentRegion(User user, SmallRegion currentRegion) {
        user.setLatestRequestRegion(currentRegion);
    }

    @Transactional
    public void updateOftenSeenRegionRefs(User user, RegionRequestDto regionRequestDto) {

        userOftenSeenRegionRepository.deleteAllByUser(user);

        for (RegionDto regionDto : regionRequestDto.getOftenSeenRegions()) {
            String bigRegionName = regionDto.getBigRegionName();
            String smallRegionName = regionDto.getSmallRegionName();

            SmallRegion smallRegion = regionService.getSmallRegionByName(bigRegionName, smallRegionName);

            UserOftenSeenRegion userOftenSeenRegion = new UserOftenSeenRegion(user, smallRegion);

            userOftenSeenRegionRepository.save(userOftenSeenRegion);
        }

    }

    public HttpHeaders createHeadersWithUserIdentification(User user) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(Global.IDENTIFICATION_TEXT, user.getIdentification());
        return responseHeaders;
    }

    private Optional<User> getUserByIdentification(String identification) {
        return userRepository.findByIdentification(identification);
    }

    @Transactional
    public void saveUserDevice(User user, SmallRegion requestRegion, String deviceInfo) {
        UserDevice userDevice = new UserDevice(user, requestRegion, deviceInfo);
        userDeviceRepository.save(userDevice);
    }

}
