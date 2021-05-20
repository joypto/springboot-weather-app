package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.BigRegion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash("big_region")
@NoArgsConstructor
public class BigRegionRedisVO {
    @Id
    private String admCode;

    @Indexed
    private long bigRegionId;

    private String bigRegionName;

    private String longitude;

    private String latitude;

    public BigRegionRedisVO(BigRegion bigRegion) {
        this.admCode = bigRegion.getAdmCode();

        this.bigRegionId = bigRegion.getId();
        this.bigRegionName = bigRegion.getBigRegionName();
        this.longitude = bigRegion.getLongitude();
        this.latitude = bigRegion.getLatitude();
    }

    @Override
    public String toString() {
        return "BigRegionRedisVO{" +
                "admCode='" + admCode + '\'' +
                ", bigRegionId=" + bigRegionId +
                ", bigRegionName='" + bigRegionName + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }

}
