package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.info.WeatherDayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherDayInfoRepository extends JpaRepository<WeatherDayInfo, Long> {
}
