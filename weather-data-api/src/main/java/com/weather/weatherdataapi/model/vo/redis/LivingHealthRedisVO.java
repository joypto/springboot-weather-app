package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter
@RedisHash("living_health")
public class LivingHealthRedisVO {
    @Id
    private final String smallRegionName;

    private final long id;

    private final long smallRegionId;

    private final long bigRegionId;

    private final String date;

    private final String areaNo;

    private final String uvToday;

    private final String uvTomorrow;

    private final String uvTheDayAfterTomorrow;

    private final String oakPollenRiskToday;

    private final String oakPollenRiskTomorrow;

    private final String oakPollenRiskTheDayAfterTomorrow;

    private final String foodPoisonToday;

    private final String foodPoisonTomorrow;

    private final String foodPoisonTheDayAfterTomorrow;

    private final String asthmaToday;

    private final String asthmaTomorrow;

    private final String asthmaTheDayAfterTomorrow;

    public LivingHealthRedisVO(LivingHealthInfo livingHealthInfo) {
        this.smallRegionName = livingHealthInfo.getSmallRegion().getSmallRegionName();

        this.id = livingHealthInfo.getId();
        this.smallRegionId = livingHealthInfo.getSmallRegion().getId();
        this.bigRegionId = livingHealthInfo.getBigRegion().getId();
        this.date = livingHealthInfo.getDate();
        this.areaNo = livingHealthInfo.getAreaNo();
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
}
