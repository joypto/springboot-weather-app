package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import com.weather.weatherdataapi.model.entity.info.WeatherDayInfo;
import com.weather.weatherdataapi.model.entity.info.WeatherWeekInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SmallRegion {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "small_region_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "big_region_id")
    private BigRegion bigRegion;

    @JsonIgnore
    @OneToMany(mappedBy = "smallRegion")
    @OrderBy("dateTime DESC")
    private List<AirPollutionInfo> airPollutionInfoList;

    @JsonIgnore
    @OneToMany(mappedBy = "smallRegion")
    private List<AirPollutionStation> airpollutionstationInfoList;

    @JsonIgnore
    @OneToMany(mappedBy = "smallRegion")
    @OrderBy("date DESC")
    private List<LivingHealthInfo> livingHealthInfoList;

    @JsonIgnore
    @OneToMany(mappedBy = "smallRegion")
    @OrderBy("createdAt DESC")
    private List<WeatherDayInfo> weatherDayInfoList;

    @JsonIgnore
    @OneToMany(mappedBy = "smallRegion")
    @OrderBy("createdAt DESC")
    private List<WeatherWeekInfo> weatherWeekInfoList;

    @Column(name = "small_region_name")
    private String smallRegionName;

    @Column(name = "adm_code")
    private String admCode;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    public SmallRegion(String smallRegionName, String admCode, String longitude, String latitude, BigRegion bigRegion) {
        this.smallRegionName = smallRegionName;
        this.admCode = admCode;
        this.longitude = longitude;
        this.latitude = latitude;
        this.bigRegion = bigRegion;
    }

}
