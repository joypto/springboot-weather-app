package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoronaInfoRepository extends JpaRepository<CoronaInfo, Long> {

    CoronaInfo findFirstByBigRegionOrderByCreatedAt(BigRegion bigRegion);
}
