package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CoronaInfoRepository extends JpaRepository<CoronaInfo, Long> {

    Optional<CoronaInfo> findFirstByBigRegionOrderByCreatedAtDesc(BigRegion bigRegion);

    Optional<CoronaInfo> findFirstByOrderByCreatedAtDesc();

    List<CoronaInfo> findAllByDate(LocalDate date);
}
