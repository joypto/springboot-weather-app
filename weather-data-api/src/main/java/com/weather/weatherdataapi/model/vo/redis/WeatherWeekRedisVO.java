package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.WeatherWeekInfo;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.List;

@Getter
@RedisHash("weather_week")
public class WeatherWeekRedisVO {
    @Id
    private final String smallRegionName;

    private final long id;

    private final long smallRegionId;

    private final List<String> maxTmp;

    private final List<String> minTmp;

    private final List<String> tmp;

    private final List<String> humidity;

    private final List<String> weather;

    private final List<String> weatherDes;

    private final List<String> rainPer;

    private final List<String> windSpeed;

    private final List<String> weatherIcon;

    private final List<String> rain;

    public WeatherWeekRedisVO(WeatherWeekInfo weatherWeekInfo) {
        this.smallRegionName = weatherWeekInfo.getSmallRegion().getSmallRegionName();

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
