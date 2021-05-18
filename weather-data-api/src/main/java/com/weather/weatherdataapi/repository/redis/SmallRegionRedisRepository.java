package com.weather.weatherdataapi.repository.redis;

import com.weather.weatherdataapi.model.vo.redis.SmallRegionRedisVO;
import org.springframework.data.repository.CrudRepository;

public interface SmallRegionRedisRepository extends CrudRepository<SmallRegionRedisVO, String> {

    SmallRegionRedisVO findBySmallRegionId(Long id);

}
