package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.Global;
import com.weather.weatherdataapi.model.dto.responsedto.MessageResponseDto;
import com.weather.weatherdataapi.service.IconService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class IconController {

    private final IconService iconService;

    @GetMapping("/api/icon/messages")
    public ResponseEntity<MessageResponseDto> getRandomIconMessage(
            @RequestParam(value = "icon") String icon, @RequestHeader(value = Global.IDENTIFICATION_TEXT, required = false) String identification,
            @RequestHeader(value = "user-agent") String userInfo) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(Global.IDENTIFICATION_TEXT, identification);

        log.info("접속한 user 기록입니다: " + userInfo);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(iconService.getRandomIconMessage(icon));
    }
}
