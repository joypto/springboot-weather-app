package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.CoronaRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface CoronaRedisRepository extends CrudRepository<CoronaRedisVO, String> {
}
