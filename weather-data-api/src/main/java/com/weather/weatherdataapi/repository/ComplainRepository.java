package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.Complain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplainRepository extends JpaRepository<Complain, Long> {
}
