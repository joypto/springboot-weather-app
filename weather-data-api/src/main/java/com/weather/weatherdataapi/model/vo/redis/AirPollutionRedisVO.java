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
    private String smallRegionAdmCode;

    private long id;

    private long smallRegionId;

    private LocalDateTime dateTime;

    private Integer pm10Value;

    private Integer pm25Value;

    public AirPollutionRedisVO(AirPollutionInfo airPollutionInfo) {
        this.smallRegionAdmCode = airPollutionInfo.getSmallRegion().getAdmCode();

        this.id = airPollutionInfo.getId();
        this.smallRegionId = airPollutionInfo.getSmallRegion().getId();
        this.dateTime = airPollutionInfo.getDateTime();
        this.pm10Value = airPollutionInfo.getPm10Value();
        this.pm25Value = airPollutionInfo.getPm25Value();
    }
}
