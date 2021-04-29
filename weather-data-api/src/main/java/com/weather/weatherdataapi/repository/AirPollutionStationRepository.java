package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.AirPollutionStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirPollutionStationRepository extends JpaRepository<AirPollutionStation, Long> {
}
