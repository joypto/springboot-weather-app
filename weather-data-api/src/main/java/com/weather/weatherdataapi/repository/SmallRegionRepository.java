package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmallRegionRepository extends JpaRepository<SmallRegion, Long> {

    Optional<SmallRegion> findByAdmCode(String admCode);

    Optional<SmallRegion> findByBigRegionAndSmallRegionName(BigRegion bigRegion, String smallRegionName);
}
