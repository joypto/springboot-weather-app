package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.LivingHealthWeather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivingHealthWeatherRepository extends JpaRepository<LivingHealthWeather, Long> {
}
