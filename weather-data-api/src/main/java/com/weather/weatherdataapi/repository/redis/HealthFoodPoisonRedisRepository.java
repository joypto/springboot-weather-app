package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.HealthFoodPoisonRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface HealthFoodPoisonRedisRepository extends CrudRepository<HealthFoodPoisonRedisVO, String> {
}
