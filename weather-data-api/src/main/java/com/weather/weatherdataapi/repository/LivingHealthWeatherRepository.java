package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.info.LivingHealthWeather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivingHealthWeatherRepository extends JpaRepository<LivingHealthWeather, Long> {
    LivingHealthWeather findByAreaNo(String areaNo);
}
