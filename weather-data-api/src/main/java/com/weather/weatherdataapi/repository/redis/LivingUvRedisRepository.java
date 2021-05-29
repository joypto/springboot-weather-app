package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.LivingUvRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface LivingUvRedisRepository extends CrudRepository<LivingUvRedisVO, String> {
}
