package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.BigRegion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("big_region")
@NoArgsConstructor
public class BigRegionRedisVO {
    @Id
    private String admCode;

    private long id;

    private String bigRegionName;

    private String longitude;

    private String latitude;

    public BigRegionRedisVO(BigRegion bigRegion) {
        this.admCode = bigRegion.getAdmCode();

        this.id = bigRegion.getId();
        this.bigRegionName = bigRegion.getBigRegionName();
        this.longitude = bigRegion.getLongitude();
        this.latitude = bigRegion.getLatitude();
    }
}
