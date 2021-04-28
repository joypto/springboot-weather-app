package com.weather.weatherdataapi.repository.region;

import com.weather.weatherdataapi.model.entity.region.RegionDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionDoRepository extends JpaRepository<RegionDo, Long> {
}