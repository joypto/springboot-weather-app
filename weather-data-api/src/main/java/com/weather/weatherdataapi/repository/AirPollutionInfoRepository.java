package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirPollutionInfoRepository extends JpaRepository<AirPollutionInfo, Long> {
}
