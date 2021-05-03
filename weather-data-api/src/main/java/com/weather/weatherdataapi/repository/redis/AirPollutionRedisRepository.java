package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.AirPollutionRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface AirPollutionRedisRepository extends CrudRepository<AirPollutionRedisVO, String> {
}
