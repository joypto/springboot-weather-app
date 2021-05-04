package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.LivingHealthRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface LivingHealthRedisRepository extends CrudRepository<LivingHealthRedisVO, String> {
}
