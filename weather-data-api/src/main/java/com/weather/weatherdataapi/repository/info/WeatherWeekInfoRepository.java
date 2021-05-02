package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.WeatherWeekInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherWeekInfoRepository extends JpaRepository<WeatherWeekInfo, Long> {

    WeatherWeekInfo findFirstBySmallRegionOrderByCreatedAt(SmallRegion smallRegion);
}
