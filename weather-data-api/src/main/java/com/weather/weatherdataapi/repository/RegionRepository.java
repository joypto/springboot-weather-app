package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findByBigRegionAndSmallRegion(String bigRegion, String smallRegion);
}
