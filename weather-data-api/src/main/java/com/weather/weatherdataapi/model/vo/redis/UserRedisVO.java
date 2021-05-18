package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("user")
@NoArgsConstructor
public class UserRedisVO {
    @Id
    private String identification;

    private Long id;

    private Long latestRequestRegionId;

    private int temp;

    private int rainPer;

    private int weather;

    private int humidity;

    private int wind;

    private int pm10;

    private int pm25;

    private int corona;

    private int uv;

    private int pollenRisk;

    private int asthma;

    private int foodPoison;

    public UserRedisVO(User user) {
        this.identification = user.getIdentification();

        this.id = user.getId();
        this.latestRequestRegionId = user.getLatestRequestRegion() != null ? user.getLatestRequestRegion().getId() : null;
        this.temp = user.getTemp();
        this.rainPer = user.getRainPer();
        this.weather = user.getWeather();
        this.humidity = user.getHumidity();
        this.wind = user.getWind();
        this.pm10 = user.getPm10();
        this.pm25 = user.getPm25();
        this.corona = user.getCorona();
        this.uv = user.getUv();
        this.pollenRisk = user.getPollenRisk();
        this.asthma = user.getAsthma();
        this.foodPoison = user.getFoodPoison();
    }

}
