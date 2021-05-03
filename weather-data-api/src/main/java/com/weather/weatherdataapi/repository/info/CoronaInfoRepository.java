package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CoronaInfoRepository extends JpaRepository<CoronaInfo, Long> {

    CoronaInfo findFirstByBigRegionOrderByCreatedAt(BigRegion bigRegion);

    CoronaInfo findFirstByOrderByCreatedAt();

    List<CoronaInfo> findAllByDate(LocalDate date);
}
