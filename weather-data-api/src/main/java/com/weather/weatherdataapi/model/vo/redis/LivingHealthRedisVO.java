package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;

@Getter
@RedisHash("living_health")
@NoArgsConstructor
public class LivingHealthRedisVO {
    @Id
    private String bigRegionAdmCode;

    private long id;

    private long bigRegionId;

    private LocalDate date;

    private Integer uvToday;

    private Integer uvTomorrow;

    private Integer uvTheDayAfterTomorrow;

    private Integer oakPollenRiskToday;

    private Integer oakPollenRiskTomorrow;

    private Integer oakPollenRiskTheDayAfterTomorrow;

    private Integer foodPoisonToday;

    private Integer foodPoisonTomorrow;

    private Integer foodPoisonTheDayAfterTomorrow;

    private Integer asthmaToday;

    private Integer asthmaTomorrow;

    private Integer asthmaTheDayAfterTomorrow;

    public LivingHealthRedisVO(LivingHealthInfo livingHealthInfo) {
        this.bigRegionAdmCode = livingHealthInfo.getBigRegion().getAdmCode();

        this.id = livingHealthInfo.getId();
        this.bigRegionId = livingHealthInfo.getBigRegion().getId();
        this.date = livingHealthInfo.getDate();
        this.uvToday = livingHealthInfo.getUvToday();
        this.uvTomorrow = livingHealthInfo.getUvTomorrow();
        this.uvTheDayAfterTomorrow = livingHealthInfo.getUvTheDayAfterTomorrow();
        this.oakPollenRiskToday = livingHealthInfo.getOakPollenRiskToday();
        this.oakPollenRiskTomorrow = livingHealthInfo.getOakPollenRiskTomorrow();
        this.oakPollenRiskTheDayAfterTomorrow = livingHealthInfo.getOakPollenRiskTheDayAfterTomorrow();
        this.foodPoisonToday = livingHealthInfo.getFoodPoisonToday();
        this.foodPoisonTomorrow = livingHealthInfo.getFoodPoisonTomorrow();
        this.foodPoisonTheDayAfterTomorrow = livingHealthInfo.getFoodPoisonTheDayAfterTomorrow();
        this.asthmaToday = livingHealthInfo.getAsthmaToday();
        this.asthmaTomorrow = livingHealthInfo.getAsthmaTomorrow();
        this.asthmaTheDayAfterTomorrow = livingHealthInfo.getAsthmaTheDayAfterTomorrow();
    }

    @Override
    public String toString() {
        return "LivingHealthRedisVO{" +
                "bigRegionAdmCode='" + bigRegionAdmCode + '\'' +
                ", id=" + id +
                ", bigRegionId=" + bigRegionId +
                ", date='" + date + '\'' +
                ", uvToday='" + uvToday + '\'' +
                ", uvTomorrow='" + uvTomorrow + '\'' +
                ", uvTheDayAfterTomorrow='" + uvTheDayAfterTomorrow + '\'' +
                ", oakPollenRiskToday='" + oakPollenRiskToday + '\'' +
                ", oakPollenRiskTomorrow='" + oakPollenRiskTomorrow + '\'' +
                ", oakPollenRiskTheDayAfterTomorrow='" + oakPollenRiskTheDayAfterTomorrow + '\'' +
                ", foodPoisonToday='" + foodPoisonToday + '\'' +
                ", foodPoisonTomorrow='" + foodPoisonTomorrow + '\'' +
                ", foodPoisonTheDayAfterTomorrow='" + foodPoisonTheDayAfterTomorrow + '\'' +
                ", asthmaToday='" + asthmaToday + '\'' +
                ", asthmaTomorrow='" + asthmaTomorrow + '\'' +
                ", asthmaTheDayAfterTomorrow='" + asthmaTheDayAfterTomorrow + '\'' +
                '}';
    }

}
