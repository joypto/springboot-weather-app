package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivingHealthInfoRepository extends JpaRepository<LivingHealthInfo, Long> {
    Optional<LivingHealthInfo> findByAreaNo(String areaNo);

    Optional<LivingHealthInfo> findFirstByBigRegionOrderByCreatedAtDesc(BigRegion bigRegion);

    Optional<LivingHealthInfo> findSecondByAreaNoOrderByCreatedAtDesc(String areaNo);

    Optional<LivingHealthInfo> findFirstByOrderByCreatedAtDesc();
}
