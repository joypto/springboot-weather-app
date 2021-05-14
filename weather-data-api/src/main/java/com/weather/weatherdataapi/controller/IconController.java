package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.Global;
import com.weather.weatherdataapi.service.IconService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class IconController {

    private final IconService iconService;

    @GetMapping("/api/icon/messages")
    public ResponseEntity<String> getRandomIconMessage(@RequestParam(value = "icon") String icon, @RequestHeader(value = Global.IDENTIFICATION_TEXT, required = false) String identification) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(Global.IDENTIFICATION_TEXT, identification);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(iconService.getRandomIconMessage(icon));
    }

}
