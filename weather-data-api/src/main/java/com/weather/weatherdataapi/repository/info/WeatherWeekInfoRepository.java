package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.WeatherWeekInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherWeekInfoRepository extends JpaRepository<WeatherWeekInfo, Long> {

    Optional<WeatherWeekInfo> findFirstBySmallRegionOrderByCreatedAtDesc(SmallRegion smallRegion);
}
