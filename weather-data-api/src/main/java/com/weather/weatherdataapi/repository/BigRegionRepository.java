package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.BigRegion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BigRegionRepository extends JpaRepository<BigRegion, Long> {

    BigRegion findByAdmCode(String admCode);

    BigRegion findByBigRegionName(String bigRegionName);
}
