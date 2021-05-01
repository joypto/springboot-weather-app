package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.SmallRegion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmallRegionRepository extends JpaRepository<SmallRegion, Long> {
}
