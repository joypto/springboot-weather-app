package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.WeatherDayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherDayInfoRepository extends JpaRepository<WeatherDayInfo, Long> {

    Optional<WeatherDayInfo> findFirstBySmallRegionOrderByCreatedAtDesc(SmallRegion smallRegion);
}
