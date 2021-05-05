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
    private String bigRegionName;

    private long id;

    private String admCode;

    private String longitude;

    private String latitude;

    public BigRegionRedisVO(BigRegion bigRegion) {
        this.bigRegionName = bigRegion.getBigRegionName();

        this.id = bigRegion.getId();
        this.admCode = bigRegion.getAdmCode();
        this.longitude = bigRegion.getLongitude();
        this.latitude = bigRegion.getLatitude();
    }
}
