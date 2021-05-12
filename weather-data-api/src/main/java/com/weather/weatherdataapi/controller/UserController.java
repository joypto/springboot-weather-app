package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.dto.requestdto.RegionRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserPreferenceResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.UserRegionResponseDto;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.service.TotalDataService;
import com.weather.weatherdataapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final TotalDataService totalDataService;

    @GetMapping("/api/user/preferences")
    public ResponseEntity<UserPreferenceResponseDto> getUserPreference(@RequestHeader(value = "identification", required = false) String identification) {
        log.info("identification='{}'", identification);

        User user = userService.getOrCreateUserByIdentification(identification);
        HttpHeaders responseHeaders = userService.createHeadersWithUserIdentification(user);

        UserPreferenceResponseDto responseDto = new UserPreferenceResponseDto(user);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(responseDto);
    }

    @GetMapping("/api/user/regions")
    public ResponseEntity<UserRegionResponseDto> getUserRegion(CoordinateDto coordinateDto, @RequestHeader(value = "identification", required = false) String identification) throws ParseException {
        log.info("identification='{}' \t coordinate={}", identification, coordinateDto.toString());

        User user = userService.getOrCreateUserByIdentification(identification);
        HttpHeaders responseHeaders = userService.createHeadersWithUserIdentification(user);

        // TODO: 서비스 코드에서 userRegionResponseDto의 값을 모두 설정한 채로 내려줬으면 하는데, 어떻게 코드를 정리하는게 좋을까?
        UserRegionResponseDto userRegionResponseDto = userService.getMyRegion(user);
        userRegionResponseDto.setCurrentRegion(totalDataService.getRegionName(coordinateDto));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(userRegionResponseDto);
    }

    @PostMapping("/api/user/preferences")
    public ResponseEntity<String> updateUserPreference(@RequestBody ScoreWeightDto scoreWeightDto, @RequestHeader(value = "identification", required = false) String identification) {
        log.info("identification='{}' \t scoreWeight={}", identification, scoreWeightDto.toString());

        User user = userService.getOrCreateUserByIdentification(identification);
        HttpHeaders responseHeaders = userService.createHeadersWithUserIdentification(user);

        userService.updatePreference(user, scoreWeightDto);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("Success");
    }
    
    @PostMapping("/api/user/regions")
    public ResponseEntity<String> updateMyRegion(@RequestBody RegionRequestDto regionRequestDto, @RequestHeader(value = "identification", required = false) String identification) {
        log.info("identification='{}' \t region={}", identification, regionRequestDto.toString());

        User user = userService.getOrCreateUserByIdentification(identification);
        HttpHeaders responseHeaders = userService.createHeadersWithUserIdentification(user);

        userService.updateOftenSeenRegions(user, regionRequestDto);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("Success");
    }

}
