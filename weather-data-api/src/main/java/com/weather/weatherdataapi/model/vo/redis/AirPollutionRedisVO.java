package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@RedisHash("air_pollution")
public class AirPollutionRedisVO {
    @Id
    private final String smallRegionName;

    private final long id;

    private final long smallRegionId;

    private final LocalDateTime dateTime;

    private final int pm10Value;

    private final int pm25Value;

    public AirPollutionRedisVO(AirPollutionInfo airPollutionInfo) {
        this.smallRegionName = airPollutionInfo.getSmallRegion().getSmallRegionName();

        this.id = airPollutionInfo.getId();
        this.smallRegionId = airPollutionInfo.getSmallRegion().getId();
        this.dateTime = airPollutionInfo.getDateTime();
        this.pm10Value = airPollutionInfo.getPm10Value();
        this.pm25Value = airPollutionInfo.getPm25Value();
    }
}
