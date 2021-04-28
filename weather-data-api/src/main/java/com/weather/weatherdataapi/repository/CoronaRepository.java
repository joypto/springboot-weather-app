package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.Corona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoronaRepository extends JpaRepository<Corona, Long> {
}
