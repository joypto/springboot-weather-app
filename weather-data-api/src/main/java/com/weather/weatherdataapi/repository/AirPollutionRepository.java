package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.AirPollutionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirPollutionRepository extends JpaRepository<AirPollutionInfo, Long> {
}
