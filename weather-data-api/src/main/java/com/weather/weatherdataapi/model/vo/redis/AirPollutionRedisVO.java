package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@RedisHash("air_pollution")
public class AirPollutionRedisVO {
    @Id
    private String smallRegionName;

    private long id;

    private long smallRegionId;

    private LocalDateTime dateTime;

    private int pm10Value;

    private int pm25Value;

    public AirPollutionRedisVO(AirPollutionInfo airPollutionInfo) {
        this.smallRegionName = airPollutionInfo.getSmallRegion().getSmallRegionName();

        this.id = airPollutionInfo.getId();
        this.smallRegionId = airPollutionInfo.getSmallRegion().getId();
        this.dateTime = airPollutionInfo.getDateTime();
        this.pm10Value = airPollutionInfo.getPm10Value();
        this.pm25Value = airPollutionInfo.getPm25Value();
    }
}
