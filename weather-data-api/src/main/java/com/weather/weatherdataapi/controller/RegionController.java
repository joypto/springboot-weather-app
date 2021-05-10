package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class RegionController {

    private final BigRegionRepository bigRegionRepository;

    @GetMapping("/api/regions")
    public List<BigRegion> getAllRegionName() {
        return bigRegionRepository.findAll();
    }

}
