package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.UserRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<UserRedisVO, String> {
}
