package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.responsedto.RegionResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.repository.SmallRegionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class RegionController {

    private final BigRegionRepository bigRegionRepository;
    private final SmallRegionRepository smallRegionRepository;

    @GetMapping("/api/region/smallregions")
    public List<SmallRegion> getSmallRegionNamesByBigRegionName(@RequestParam("currentBigRegionName") String bigRegionName) {
        BigRegion bigRegion = bigRegionRepository.findByBigRegionName(bigRegionName);
        return bigRegion.getSmallRegionList();
    }

    @GetMapping("/api/region")
    public RegionResponseDto getAllRegions() {
        RegionResponseDto regionResponseDto = new RegionResponseDto();
        regionResponseDto.setBigRegionList(bigRegionRepository.findAll());
        regionResponseDto.setSmallRegionList(smallRegionRepository.findAll());
        return regionResponseDto;
    }

}
