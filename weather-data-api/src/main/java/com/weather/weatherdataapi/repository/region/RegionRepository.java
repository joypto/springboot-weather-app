package com.weather.weatherdataapi.repository.region;

import com.weather.weatherdataapi.model.entity.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findBySmallRegion(String region);
}
