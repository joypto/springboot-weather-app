package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoronaInfoRepository extends JpaRepository<CoronaInfo, Long> {

    Optional<CoronaInfo> findByBigRegion(String bigRegion);
}
