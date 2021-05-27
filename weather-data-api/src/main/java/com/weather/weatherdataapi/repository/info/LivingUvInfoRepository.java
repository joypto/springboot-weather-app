package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.info.LivingUvInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivingUvInfoRepository extends JpaRepository<LivingUvInfo, Long> {
}
