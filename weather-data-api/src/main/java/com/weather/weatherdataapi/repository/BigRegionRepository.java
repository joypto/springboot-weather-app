package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.BigRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BigRegionRepository extends JpaRepository<BigRegion, Long> {

    Optional<BigRegion> findByAdmCode(String admCode);

    Optional<BigRegion> findByBigRegionName(String bigRegionName);
}
