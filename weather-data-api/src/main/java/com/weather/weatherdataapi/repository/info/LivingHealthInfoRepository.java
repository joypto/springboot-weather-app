package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LivingHealthInfoRepository extends JpaRepository<LivingHealthInfo, Long> {

    Optional<LivingHealthInfo> findFirstByBigRegionOrderByCreatedAtDesc(BigRegion bigRegion);

    Optional<LivingHealthInfo> findSecondByBigRegionOrderByCreatedAtDesc(BigRegion bigRegion);

    Optional<LivingHealthInfo> findFirstByOrderByCreatedAtDesc();

    List<LivingHealthInfo> findAllByDate(LocalDate date);
}
