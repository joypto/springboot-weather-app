package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@RedisHash("corona")
public class CoronaRedisVO {
    @Id
    private final String bigRegionName;

    private final long id;

    private final long bigRegionId;

    private final LocalDate date;

    private final int newLocalCaseCount;

    private final int newForeignCaseCount;

    public CoronaRedisVO(CoronaInfo coronaInfo) {
        this.bigRegionName = coronaInfo.getBigRegion().getBigRegionName();

        this.id = coronaInfo.getId();
        this.bigRegionId = coronaInfo.getBigRegion().getId();
        this.date = coronaInfo.getDate();
        this.newLocalCaseCount = coronaInfo.getNewLocalCaseCount();
        this.newForeignCaseCount = coronaInfo.getNewForeignCaseCount();
    }
}
