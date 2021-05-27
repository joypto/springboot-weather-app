package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.HealthPollenRiskInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;

@Getter
@RedisHash("health_pollen_risk")
@NoArgsConstructor
public class HealthPollenRiskRedisVO {

    @Id
    private String bigRegionAdmCode;

    private long id;

    private long bigRegionId;

    private LocalDate date;

    private Integer today;

    private Integer tomorrow;

    private Integer theDayAfterTomorrow;

    public HealthPollenRiskRedisVO(HealthPollenRiskInfo info) {
        this.bigRegionAdmCode = info.getBigRegion().getAdmCode();

        this.id = info.getId();
        this.date = info.getDate();
        this.today = info.getToday();
        this.tomorrow = info.getTomorrow();
        this.theDayAfterTomorrow = info.getTheDayAfterTomorrow();
    }

}
