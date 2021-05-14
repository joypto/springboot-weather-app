package com.weather.weatherdataapi.model.entity.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.Timestamped;
import com.weather.weatherdataapi.model.vo.redis.LivingHealthRedisVO;
import com.weather.weatherdataapi.util.openapi.health.asthma.AsthmaItem;
import com.weather.weatherdataapi.util.openapi.health.food_poison.FoodPoisonItem;
import com.weather.weatherdataapi.util.openapi.health.pollen_risk.PollenRiskItem;
import com.weather.weatherdataapi.util.openapi.living.uv.UvItem;
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

    public LivingHealthInfo(BigRegion bigRegion, String date, UvItem uvItem, AsthmaItem asthmaItem, FoodPoisonItem foodPoisonItem, PollenRiskItem pollenRiskItem) {
        // final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddhh");
        // this.date = dateTime.format(DATE_FORMATTER);

        this.bigRegion = bigRegion;
        this.date = date;
        this.uvToday = uvItem.getToday().toString();
        this.uvTomorrow = uvItem.getTomorrow().toString();
        this.uvTheDayAfterTomorrow = uvItem.getTheDayAfterTomorrow().toString();
        this.asthmaToday = asthmaItem.getToday();
        this.asthmaTomorrow = asthmaItem.getTomorrow();
        this.asthmaTheDayAfterTomorrow = asthmaItem.getTheDayAfterTomorrow();
        this.foodPoisonToday = foodPoisonItem.getToday();
        this.foodPoisonTomorrow = foodPoisonItem.getTomorrow();
        this.foodPoisonTheDayAfterTomorrow = foodPoisonItem.getTheDayAfterTomorrow();
        this.oakPollenRiskToday = pollenRiskItem.getToday();
        this.oakPollenRiskTomorrow = pollenRiskItem.getTomorrow();
        this.oakPollenRiskTheDayAfterTomorrow = pollenRiskItem.getTheDayAfterTomorrow();
    }

    public LivingHealthInfo(LivingHealthRedisVO livingHealthRedisVO, BigRegion bigRegion) {
        this.id = livingHealthRedisVO.getId();
        this.bigRegion = bigRegion;
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
