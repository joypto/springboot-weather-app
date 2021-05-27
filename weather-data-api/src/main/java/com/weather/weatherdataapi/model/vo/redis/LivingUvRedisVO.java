package com.weather.weatherdataapi.model.vo.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;

@Getter
@RedisHash("living_uv")
@NoArgsConstructor
public class LivingUvRedisVO {

    @Id
    private String bigRegionAdmCode;

    private long id;

    private long bigRegionId;

    private LocalDate date;

    private Integer today;

    private Integer tomorrow;

    private Integer theDayAfterTomorrow;

}
