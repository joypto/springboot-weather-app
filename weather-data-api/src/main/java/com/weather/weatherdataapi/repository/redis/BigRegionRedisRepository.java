package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.BigRegionRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface BigRegionRedisRepository extends CrudRepository<BigRegionRedisVO, String> {
}
