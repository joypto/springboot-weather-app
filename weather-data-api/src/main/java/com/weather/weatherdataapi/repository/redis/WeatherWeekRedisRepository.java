package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.WeatherWeekRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface WeatherWeekRedisRepository extends CrudRepository<WeatherWeekRedisVO, String> {
}
