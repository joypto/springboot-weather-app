package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.HealthPollenRiskRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface HealthPollenRiskRedisRepository extends CrudRepository<HealthPollenRiskRedisVO, String> {
}
