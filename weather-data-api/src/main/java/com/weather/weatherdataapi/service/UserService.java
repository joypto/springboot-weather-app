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
import com.weather.weatherdataapi.model.vo.redis.UserRedisVO;
import com.weather.weatherdataapi.repository.UserDeviceRepository;
import com.weather.weatherdataapi.repository.UserOftenSeenRegionRepository;
import com.weather.weatherdataapi.repository.UserRepository;
import com.weather.weatherdataapi.repository.redis.UserRedisRepository;
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
    private final UserRedisRepository userRedisRepository;
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

    public User getOrCreateGuaranteedNonCachedUserByIdentification(String identification) {
        if (StringUtils.hasText(identification) == false)
            return createNewUser();

        Optional<User> queriedUser = getGuaranteedNonCachedUser(identification);

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

        refreshCache(user);
    }

    @Transactional
    public void updateCurrentRegion(User user, SmallRegion currentRegion) {
        user.setLatestRequestRegion(currentRegion);

        refreshCache(user);
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

        refreshCache(user);
    }

    public HttpHeaders createHeadersWithUserIdentification(User user) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(Global.IDENTIFICATION_TEXT, user.getIdentification());
        return responseHeaders;
    }

    private Optional<User> getUserByIdentification(String identification) {
        Optional<UserRedisVO> queriedUserRedisVO = userRedisRepository.findById(identification);

        if (queriedUserRedisVO.isPresent()) {
            SmallRegion latestRequestRegion = regionService.getSmallRegionByAdmCode(queriedUserRedisVO.get().getLatestRequestRegionAdmCode());

            User user = new User(queriedUserRedisVO.get(), latestRequestRegion);
            return Optional.of(user);
        }

        Optional<User> queriedUser = userRepository.findByIdentification(identification);

        if (queriedUser.isPresent())
            refreshCache(queriedUser.get());

        return queriedUser;
    }

    /**
     * 특정 user의 캐시를 갱신합니다.
     *
     * @param user 갱신할 대상인 user 입니다.
     */
    private void refreshCache(User user) {
        if (user == null)
            return;

        removeCache(user.getIdentification());

        UserRedisVO userRedisVO = new UserRedisVO(user);
        userRedisRepository.save(userRedisVO);
    }

    private void removeCache(String identification) {
        Optional<UserRedisVO> queriedUserRedisVO = userRedisRepository.findById(identification);

        if (queriedUserRedisVO.isPresent())
            userRedisRepository.deleteById(identification);
    }

    /**
     * 캐시되지 않은 user 객체를 반환받습니다.
     * DB에 접근해 값을 직접 수정하기 위해서는 반드시 이 함수로 반환받은 user객체를 사용해야 변경될 수 있음을 보장받을 수 있습니다.
     * 이 user객체가 만약 redis에서 가져온 캐시된 유저 정보라면, 동일한 유저 정보를 DB에서 조회해옵니다.
     * 만약 이 user정보가 캐시된 값이 아니라면, 아무 처리도 하지 않고 전달받은 user를 다시 반환합니다.
     *
     * @return 캐시되지 않은 것이 보장된 user 객체입니다.
     */
    private Optional<User> getGuaranteedNonCachedUser(User user) {
        return userRepository.findByIdentification(user.getIdentification());
    }

    /**
     * 캐시되지 않은 user 객체를 반환받습니다.
     * DB에 접근해 값을 직접 수정하기 위해서는 반드시 이 함수로 반환받은 user객체를 사용해야 변경될 수 있음을 보장받을 수 있습니다.
     * 이 user객체가 만약 redis에서 가져온 캐시된 유저 정보라면, 동일한 유저 정보를 DB에서 조회해옵니다.
     * 만약 이 user정보가 캐시된 값이 아니라면, 아무 처리도 하지 않고 전달받은 user를 다시 반환합니다.
     *
     * @return 캐시되지 않은 것이 보장된 user 객체입니다.
     */
    private Optional<User> getGuaranteedNonCachedUser(String identification) {
        if (StringUtils.hasText(identification) == false)
            return Optional.empty();

        return userRepository.findByIdentification(identification);
    }

    public void saveUserDevice(String identification, RegionDto regionDto, String deviceInfo) {
        String requestRegion = regionDto.getBigRegionName() + " " + regionDto.getSmallRegionName();
        UserDevice userDevice = new UserDevice(identification, requestRegion, deviceInfo);
        userDeviceRepository.save(userDevice);
    }

}
