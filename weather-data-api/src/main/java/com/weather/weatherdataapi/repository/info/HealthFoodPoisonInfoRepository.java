package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.HealthFoodPoisonInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthFoodPoisonInfoRepository extends JpaRepository<HealthFoodPoisonInfo, Long> {

    Optional<HealthFoodPoisonInfo> findFirstByBigRegionOrderByDateDesc(BigRegion bigRegion);

}
