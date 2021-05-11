package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@RestController
public class UserPreferenceController {

    private final UserRepository userRepository;

    @PostMapping("/api/user/preferences")
    public User saveUserPreference(@RequestBody ScoreWeightDto scoreWeightDto, @RequestHeader(value = "token", required = false) String token) {
        log.info("token='{}' \t scoreWeight={}", token, scoreWeightDto.toString());

        // 이미 identification을 발급받은 클라이언트의 요청입니다.
        if (StringUtils.hasText(token)) {
            Optional<User> queriedUser = userRepository.findByIdentification(token);

            // DB에 저장되어 있는 user를 가리키는 identification라면 수정을 시도합니다.
            if (queriedUser.isPresent())
                return queriedUser.get();
        }

        // 이전에 identification을 발급받지 않은 사용자이거나,
        // DB에 저장되어 있는 User의 identification이 아니라면
        // 새로운 User를 만들어 반환합니다.
        String newIdentification = "wl" + ZonedDateTime.now().toString() + UUID.randomUUID();

        User newUser = new User(newIdentification, scoreWeightDto);
        userRepository.save(newUser);

        return newUser;
    }

}
