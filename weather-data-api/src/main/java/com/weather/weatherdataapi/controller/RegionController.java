package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.service.RegionService;
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

    private final RegionService regionService;

    @GetMapping("/api/region/smallregions")
    public List<SmallRegion> getSmallRegionNamesByBigRegionName(@RequestParam("currentBigRegionName") String bigRegionName) {
        BigRegion bigRegion = regionService.getBigRegionByName(bigRegionName);
        return bigRegion.getSmallRegionList();
    }

}
