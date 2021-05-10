package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirPollutionInfoRepository extends JpaRepository<AirPollutionInfo, Long> {

    Optional<AirPollutionInfo> findFirstBySmallRegionOrderByCreatedAtDesc(SmallRegion smallRegion);

    Optional<AirPollutionInfo> findFirstByOrderByDateTimeDesc();
}
