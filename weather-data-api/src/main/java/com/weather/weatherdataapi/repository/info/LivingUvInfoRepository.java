package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.LivingUvInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivingUvInfoRepository extends JpaRepository<LivingUvInfo, Long> {

    Optional<LivingUvInfo> findFirstByBigRegionOrderByDateDesc(BigRegion bigRegion);

}
