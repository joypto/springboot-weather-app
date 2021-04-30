package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.Corona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoronaRepository extends JpaRepository<Corona, Long> {

    Optional<Corona> findByBigRegion(String bigRegion);
}
