package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.UVIdx;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UVIdxRepository extends JpaRepository<UVIdx, Long> {
}
