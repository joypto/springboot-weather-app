package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.WeatherWeekInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.List;

@Getter
@NoArgsConstructor
@RedisHash("weather_week")
public class WeatherWeekRedisVO {
    @Id
    private String smallRegionAdmCode;
    @TimeToLive
    private long timeout;

    private long id;

    private long smallRegionId;

    private List<String> maxTmp;

    private List<String> minTmp;

    private List<String> tmp;

    private List<String> humidity;

    private List<String> weather;

    private List<String> weatherDes;

    private List<String> rainPer;

    private List<String> windSpeed;

    private List<String> weatherIcon;

    private List<String> rain;

    public WeatherWeekRedisVO(WeatherWeekInfo weatherWeekInfo, long timeout) {
        this.smallRegionAdmCode = weatherWeekInfo.getSmallRegion().getAdmCode();
        this.timeout = timeout;
        this.id = weatherWeekInfo.getId();
        this.smallRegionId = weatherWeekInfo.getSmallRegion().getId();
        this.maxTmp = weatherWeekInfo.getMaxTmp();
        this.minTmp = weatherWeekInfo.getMinTmp();
        this.tmp = weatherWeekInfo.getTmp();
        this.humidity = weatherWeekInfo.getHumidity();
        this.weather = weatherWeekInfo.getWeather();
        this.weatherDes = weatherWeekInfo.getWeatherDes();
        this.rainPer = weatherWeekInfo.getRainPer();
        this.windSpeed = weatherWeekInfo.getWindSpeed();
        this.weatherIcon = weatherWeekInfo.getWeatherIcon();
        this.rain = weatherWeekInfo.getRain();
    }
}
