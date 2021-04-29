package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.AirPollutionStation;
import com.weather.weatherdataapi.model.entity.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirPollutionStationRepository extends JpaRepository<AirPollutionStation, Long> {

    Optional<AirPollutionStation> findByRegion(Region region);
}
