package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.AirPollutionStation;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirPollutionStationRepository extends JpaRepository<AirPollutionStation, Long> {

    List<AirPollutionStation> findAllBySmallRegionOrderByDistanceAsc(SmallRegion smallRegion);
}
