package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.WeatherDayRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface WeatherDayRedisRepository extends CrudRepository<WeatherDayRedisVO, String> {
}
