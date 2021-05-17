package com.weather.weatherdataapi.model.entity.info;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.Timestamped;
import com.weather.weatherdataapi.model.vo.redis.WeatherDayRedisVO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WeatherDayInfo extends Timestamped {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "small_region_id")
    private SmallRegion smallRegion;

    // 기온
    @ElementCollection
    private List<String> tmp;
    // 날씨
    @ElementCollection
    private List<String> weather;
    // 날씨 설명
    @ElementCollection
    private List<String> weatherDes;
    // 강수확률
    @ElementCollection
    private List<String> rainPer;
    // 시간정보
    @ElementCollection
    private List<String> dailyTime;
    // 날씨 아이콘
    @ElementCollection
    private List<String> weatherIcon;


    @Builder
    public WeatherDayInfo(List<String> tmp, List<String> weather, List<String> weatherDes, List<String> rainPer, List<String> dailyTime, List<String> weatherIcon) {
        this.tmp = tmp;
        this.weather = weather;
        this.weatherDes = weatherDes;
        this.rainPer = rainPer;
        this.dailyTime = dailyTime;
        this.weatherIcon = weatherIcon;
    }

    public WeatherDayInfo(WeatherDayRedisVO weatherDayRedisVO) {
        this.tmp = weatherDayRedisVO.getTmp();
        this.weather = weatherDayRedisVO.getWeather();
        this.weatherDes = weatherDayRedisVO.getWeatherDes();
        this.rainPer = weatherDayRedisVO.getRainPer();
        this.dailyTime = weatherDayRedisVO.getDailyTime();
        this.weatherIcon = weatherDayRedisVO.getWeatherIcon();
    }

    @Override
    public String toString() {
        return "WeatherDayInfo{" +
                "id=" + id +
                ", smallRegion=" + smallRegion +
                ", tmp=" + tmp +
                ", weather=" + weather +
                ", weatherDes=" + weatherDes +
                ", rainPer=" + rainPer +
                ", dailyTime=" + dailyTime +
                ", weatherIcon=" + weatherIcon +
                '}';
    }

}
