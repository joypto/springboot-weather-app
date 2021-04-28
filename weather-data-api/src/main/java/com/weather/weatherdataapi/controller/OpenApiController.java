package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OpenApiController {

    final private OpenApiService openApiService;





























    @GetMapping("api/test")
    public void show(){
        openApiService.callApi();
    }
}
