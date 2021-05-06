package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.repository.redis.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final BigRegionRedisRepository bigRegionRedisRepository;
    private final SmallRegionRedisRepository smallRegionRedisRepository;

    private final AirPollutionRedisRepository airPollutionRedisRepository;
    private final CoronaRedisRepository coronaRedisRepository;
    private final LivingHealthRedisRepository livingHealthRedisRepository;
    private final WeatherDayRedisRepository weatherDayRedisRepository;
    private final WeatherWeekRedisRepository weatherWeekRedisRepository;

    public void initialize() {
        bigRegionRedisRepository.deleteAll();
        smallRegionRedisRepository.deleteAll();

        airPollutionRedisRepository.deleteAll();
        coronaRedisRepository.deleteAll();
        livingHealthRedisRepository.deleteAll();
        weatherDayRedisRepository.deleteAll();
        weatherWeekRedisRepository.deleteAll();
    }

}
