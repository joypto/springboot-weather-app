package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.HealthAsthmaRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface HealthAsthmaRedisRepository extends CrudRepository<HealthAsthmaRedisVO, String> {
}
