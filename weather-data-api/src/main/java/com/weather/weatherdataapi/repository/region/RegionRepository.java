package com.weather.weatherdataapi.repository.region;

import com.weather.weatherdataapi.model.entity.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findBySmallRegion(String region);
    List<Region> findByBigRegion(String region);
}
