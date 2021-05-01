package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivingHealthWeatherRepository extends JpaRepository<LivingHealthInfo, Long> {
    LivingHealthInfo findByAreaNo(String areaNo);
}
