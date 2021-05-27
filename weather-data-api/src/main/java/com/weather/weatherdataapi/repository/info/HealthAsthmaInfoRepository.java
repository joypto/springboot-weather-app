package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.HealthAsthmaInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthAsthmaInfoRepository extends JpaRepository<HealthAsthmaInfo, Long> {

    Optional<HealthAsthmaInfo> findFirstByBigRegionOrderByDateDesc(BigRegion bigRegion);

}
