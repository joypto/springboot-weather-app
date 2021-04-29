package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.AirPollution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirPollutionRepository extends JpaRepository<AirPollution, Long> {
}
