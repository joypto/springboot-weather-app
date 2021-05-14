package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.Icon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IconRepository extends JpaRepository<Icon, Long> {
    Icon findByDescription(String description);
}
