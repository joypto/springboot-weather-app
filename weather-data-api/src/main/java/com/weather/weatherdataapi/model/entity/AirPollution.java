package com.weather.weatherdataapi.model.entity;

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
public class AirPollution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_pollution_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column
    private LocalDateTime dateTime;

    @Column
    private Integer pm10Value;

    @Column
    private Integer pm25Value;

    public AirPollution(AirKoreaAirPollutionItem item, Region region) {
        this.region = region;
        this.dateTime = LocalDateTime.parse(item.getDataTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.pm10Value = Integer.parseInt(item.getPm10Value());
        this.pm25Value = Integer.parseInt(item.getPm25Value());
    }

}
