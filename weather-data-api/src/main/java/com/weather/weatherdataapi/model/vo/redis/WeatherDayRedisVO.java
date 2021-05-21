package com.weather.weatherdataapi.model.vo.redis;

import com.weather.weatherdataapi.model.entity.info.WeatherDayInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RedisHash("weather_day")
public class WeatherDayRedisVO {
    @Id
    private String smallRegionAdmCode;
    @TimeToLive
    private long timeout;

    private long id;

    private long smallRegionId;

    private List<String> tmp;

    private List<String> weather;

    private List<String> weatherDes;

    private List<String> rainPer;

    private List<String> dailyTime;

    private List<String> weatherIcon;

    public WeatherDayRedisVO(WeatherDayInfo weatherDayInfo, long timeout) {
        this.smallRegionAdmCode = weatherDayInfo.getSmallRegion().getAdmCode();
        this.timeout = timeout;
        this.id = weatherDayInfo.getId();
        this.smallRegionId = weatherDayInfo.getSmallRegion().getId();
        this.tmp = weatherDayInfo.getTmp();
        this.weather = weatherDayInfo.getWeather();
        this.weatherDes = weatherDayInfo.getWeatherDes();
        this.rainPer = weatherDayInfo.getRainPer();
        this.dailyTime = weatherDayInfo.getDailyTime();
        this.weatherIcon = weatherDayInfo.getWeatherIcon();
    }

    @Override
    public String toString() {
        return "WeatherDayRedisVO{" +
                "smallRegionAdmCode='" + smallRegionAdmCode + '\'' +
                ", id=" + id +
                ", smallRegionId=" + smallRegionId +
                ", tmp=" + tmp +
                ", weather=" + weather +
                ", weatherDes=" + weatherDes +
                ", rainPer=" + rainPer +
                ", dailyTime=" + dailyTime +
                ", weatherIcon=" + weatherIcon +
                '}';
    }

}
