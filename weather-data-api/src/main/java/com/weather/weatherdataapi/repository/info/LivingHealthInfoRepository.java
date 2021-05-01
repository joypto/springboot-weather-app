package com.weather.weatherdataapi.repository.info;

import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivingHealthInfoRepository extends JpaRepository<LivingHealthInfo, Long> {
    LivingHealthInfo findByAreaNo(String areaNo);
}
