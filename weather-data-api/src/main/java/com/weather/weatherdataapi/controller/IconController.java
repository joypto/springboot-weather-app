package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.service.IconService;
import com.weather.weatherdataapi.util.IconUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
public class IconController {

    private final IconService iconService;

    @GetMapping("/api/icon/messages")
    public String getRandomIconMessage(@RequestParam(value = "icon") String icon) {
        return iconService.getRandomIconMessage(icon);
    }

}
