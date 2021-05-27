package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.LivingUvInfo;
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

    public LivingUvRedisVO(LivingUvInfo info) {
        this.bigRegionAdmCode = info.getBigRegion().getAdmCode();

        this.id = info.getId();
        this.bigRegionId = info.getBigRegion().getId();
        this.date = info.getDate();
        this.today = info.getToday();
        this.tomorrow = info.getTomorrow();
        this.theDayAfterTomorrow = info.getTheDayAfterTomorrow();
    }

}
