package com.weather.weatherdataapi.model.entity.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.Timestamped;
import com.weather.weatherdataapi.model.vo.redis.LivingHealthRedisVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LivingHealthInfo extends Timestamped {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "big_region_id")
    private BigRegion bigRegion;

    @JsonIgnore
    @Column
    private String date;

    @JsonIgnore
    @Column
    private String areaNo;

    @Column
    private String uvToday;

    @Column
    private String uvTomorrow;

    @Column
    private String uvTheDayAfterTomorrow;

    @Column
    private String oakPollenRiskToday;

    @Column
    private String oakPollenRiskTomorrow;

    @Column
    private String oakPollenRiskTheDayAfterTomorrow;

    @Column
    private String foodPoisonToday;

    @Column
    private String foodPoisonTomorrow;

    @Column
    private String foodPoisonTheDayAfterTomorrow;

    @Column
    private String asthmaToday;

    @Column
    private String asthmaTomorrow;

    @Column
    private String asthmaTheDayAfterTomorrow;

    public LivingHealthInfo(LivingHealthRedisVO livingHealthRedisVO, BigRegion bigRegion) {
        this.id = livingHealthRedisVO.getId();
        this.bigRegion = bigRegion;
        this.areaNo = livingHealthRedisVO.getAreaNo();
        this.uvToday = livingHealthRedisVO.getUvToday();
        this.uvTomorrow = livingHealthRedisVO.getUvTomorrow();
        this.uvTheDayAfterTomorrow = livingHealthRedisVO.getUvTheDayAfterTomorrow();
        this.oakPollenRiskToday = livingHealthRedisVO.getOakPollenRiskToday();
        this.oakPollenRiskTomorrow = livingHealthRedisVO.getOakPollenRiskTomorrow();
        this.oakPollenRiskTheDayAfterTomorrow = livingHealthRedisVO.getOakPollenRiskTheDayAfterTomorrow();
        this.foodPoisonToday = livingHealthRedisVO.getFoodPoisonToday();
        this.foodPoisonTomorrow = livingHealthRedisVO.getFoodPoisonTomorrow();
        this.foodPoisonTheDayAfterTomorrow = livingHealthRedisVO.getFoodPoisonTheDayAfterTomorrow();
        this.asthmaToday = livingHealthRedisVO.getAsthmaToday();
        this.asthmaTomorrow = livingHealthRedisVO.getAsthmaTomorrow();
        this.asthmaTheDayAfterTomorrow = livingHealthRedisVO.getAsthmaTheDayAfterTomorrow();
    }

    @Override
    public String toString() {
        return "LivingHealthInfo{" +
                "id=" + id +
                ", bigRegion=" + bigRegion +
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
