package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.vo.redis.SmallRegionRedisVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(name = "small_region_name")
    private String smallRegionName;

    @JsonIgnore
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

    public SmallRegion(SmallRegionRedisVO smallRegionRedisVO, BigRegion bigRegion) {
        this.id = smallRegionRedisVO.getId();
        this.bigRegion = bigRegion;
        this.smallRegionName = smallRegionRedisVO.getSmallRegionName();
        this.admCode = smallRegionRedisVO.getAdmCode();
        this.longitude = smallRegionRedisVO.getLongitude();
        this.latitude = smallRegionRedisVO.getLatitude();
    }

}
