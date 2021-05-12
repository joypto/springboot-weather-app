package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.Global;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class RegionController {

    private final BigRegionRepository bigRegionRepository;

    @GetMapping("/api/regions")
    public ResponseEntity<List<BigRegion>> getAllRegionName(@RequestHeader(value = Global.IDENTIFICATION_TEXT, required = false) String identification) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(Global.IDENTIFICATION_TEXT, identification);
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(bigRegionRepository.findAll());
    }

}
