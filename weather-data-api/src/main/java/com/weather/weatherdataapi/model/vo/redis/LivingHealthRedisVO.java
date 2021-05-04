package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("living_health")
@NoArgsConstructor
public class LivingHealthRedisVO {
    @Id
    private String bigRegionName;

    private long id;

    private long bigRegionId;

    private String date;

    private String areaNo;

    private String uvToday;

    private String uvTomorrow;

    private String uvTheDayAfterTomorrow;

    private String oakPollenRiskToday;

    private String oakPollenRiskTomorrow;

    private String oakPollenRiskTheDayAfterTomorrow;

    private String foodPoisonToday;

    private String foodPoisonTomorrow;

    private String foodPoisonTheDayAfterTomorrow;

    private String asthmaToday;

    private String asthmaTomorrow;

    private String asthmaTheDayAfterTomorrow;

    public LivingHealthRedisVO(LivingHealthInfo livingHealthInfo) {
        this.bigRegionName = livingHealthInfo.getBigRegion().getBigRegionName();

        this.id = livingHealthInfo.getId();
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
