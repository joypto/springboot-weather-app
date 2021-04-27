package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.WeekInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekInfoRepository extends JpaRepository<WeekInfo, Long> {
}
