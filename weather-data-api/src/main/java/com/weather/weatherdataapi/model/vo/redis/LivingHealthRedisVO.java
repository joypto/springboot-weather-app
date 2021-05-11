package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("living_health")
@NoArgsConstructor
public class LivingHealthRedisVO {
    @Id
    private String bigRegionAdmCode;

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
        this.bigRegionAdmCode = livingHealthInfo.getBigRegion().getAdmCode();

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

    @Override
    public String toString() {
        return "LivingHealthRedisVO{" +
                "bigRegionAdmCode='" + bigRegionAdmCode + '\'' +
                ", id=" + id +
                ", bigRegionId=" + bigRegionId +
                ", date='" + date + '\'' +
                ", areaNo='" + areaNo + '\'' +
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
