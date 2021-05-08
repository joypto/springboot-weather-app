package com.weather.weatherdataapi.model.entity.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.Timestamped;
import com.weather.weatherdataapi.model.vo.redis.AirPollutionRedisVO;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AirPollutionInfo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_pollution_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "small_region_id")
    private SmallRegion smallRegion;

    @Column
    private LocalDateTime dateTime;

    @Column
    private Integer pm10Value;

    @Column
    private Integer pm25Value;

    public AirPollutionInfo(AirKoreaAirPollutionItem item, SmallRegion smallRegion) {
        this.smallRegion = smallRegion;
        this.dateTime = LocalDateTime.parse(item.getDataTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        try {
            this.pm10Value = Integer.parseInt(item.getPm10Value());
        } catch (Exception e) {
            this.pm10Value = null;
        }

        try {
            this.pm25Value = Integer.parseInt(item.getPm25Value());
        } catch (Exception e) {
            this.pm25Value = null;
        }

    }

    public AirPollutionInfo(AirPollutionRedisVO airPollutionRedisVO, SmallRegion smallRegion) {
        this.id = airPollutionRedisVO.getId();
        this.smallRegion = smallRegion;
        this.dateTime = airPollutionRedisVO.getDateTime();
        this.pm10Value = airPollutionRedisVO.getPm10Value();
        this.pm25Value = airPollutionRedisVO.getPm25Value();
    }
}
