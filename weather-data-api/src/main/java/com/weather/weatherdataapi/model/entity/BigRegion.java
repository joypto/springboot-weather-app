package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import com.weather.weatherdataapi.model.vo.redis.BigRegionRedisVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BigRegion {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "big_region_id")
    private Long id;

    @OneToMany(mappedBy = "bigRegion")
    private List<SmallRegion> smallRegionList;

    @JsonIgnore
    @OneToMany(mappedBy = "bigRegion")
    @OrderBy("date DESC")
    private List<CoronaInfo> coronaInfoList;

    @JsonIgnore
    @OneToMany(mappedBy = "bigRegion")
    @OrderBy("createdAt DESC")
    private List<LivingHealthInfo> livingHealthInfoList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "big_region_name")
    private String bigRegionName;

    @JsonIgnore
    @Column(name = "adm_code")
    private String admCode;

    @JsonIgnore
    @Column(name = "longitude")
    private String longitude;

    @JsonIgnore
    @Column(name = "latitude")
    private String latitude;

    public BigRegion(String bigRegionName, String admCode, String longitude, String latitude) {
        this.bigRegionName = bigRegionName;
        this.admCode = admCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public BigRegion(BigRegionRedisVO regionRedisVO) {
        this.id = regionRedisVO.getId();
        this.bigRegionName = regionRedisVO.getBigRegionName();
        this.admCode = regionRedisVO.getAdmCode();
        this.longitude = regionRedisVO.getLongitude();
        this.latitude = regionRedisVO.getLatitude();
    }

}
