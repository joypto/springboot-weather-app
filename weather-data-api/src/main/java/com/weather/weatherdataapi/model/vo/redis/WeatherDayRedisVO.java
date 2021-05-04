package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.WeatherDayInfo;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.List;

@Getter
@RedisHash("weather_day")
public class WeatherDayRedisVO {
    @Id
    private final String smallRegionName;

    private final long id;

    private final long smallRegionId;

    private final List<String> tmp;

    private final List<String> weather;

    private final List<String> weatherDes;

    private final List<String> rainPer;

    private final List<String> dailyTime;

    private final List<String> weatherIcon;

    public WeatherDayRedisVO(WeatherDayInfo weatherDayInfo) {
        this.smallRegionName = weatherDayInfo.getSmallRegion().getSmallRegionName();

        this.id = weatherDayInfo.getId();
        this.smallRegionId = weatherDayInfo.getSmallRegion().getId();
        this.tmp = weatherDayInfo.getTmp();
        this.weather = weatherDayInfo.getWeather();
        this.weatherDes = weatherDayInfo.getWeatherDes();
        this.rainPer = weatherDayInfo.getRainPer();
        this.dailyTime = weatherDayInfo.getDailyTime();
        this.weatherIcon = weatherDayInfo.getWeatherIcon();
    }
}
