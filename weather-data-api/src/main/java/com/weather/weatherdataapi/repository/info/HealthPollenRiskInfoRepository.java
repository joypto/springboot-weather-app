package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.HealthPollenRiskInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthPollenRiskInfoRepository extends JpaRepository<HealthPollenRiskInfo, Long> {

    Optional<HealthPollenRiskInfo> findFirstByBigRegionOrderByDateDesc(BigRegion bigRegion);

}
