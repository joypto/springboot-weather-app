package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.repository.redis.*;
import com.weather.weatherdataapi.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private final UserRedisRepository userRedisRepository;

    private final BigRegionRedisRepository bigRegionRedisRepository;
    private final SmallRegionRedisRepository smallRegionRedisRepository;

    private final AirPollutionRedisRepository airPollutionRedisRepository;
    private final CoronaRedisRepository coronaRedisRepository;
    private final LivingHealthRedisRepository livingHealthRedisRepository;
    private final WeatherDayRedisRepository weatherDayRedisRepository;
    private final WeatherWeekRedisRepository weatherWeekRedisRepository;

    @Transactional
    public void initialize() {
        try {
            userRedisRepository.deleteAll();

            bigRegionRedisRepository.deleteAll();
            smallRegionRedisRepository.deleteAll();

            airPollutionRedisRepository.deleteAll();
            coronaRedisRepository.deleteAll();
            livingHealthRedisRepository.deleteAll();
            weatherDayRedisRepository.deleteAll();
            weatherWeekRedisRepository.deleteAll();

            log.info("Redis DB를 성공적으로 초기화하였습니다.");
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));
            log.error("Redis DB를 초기화하는 중 문제가 발생하였습니다.");
            throw e;
        }

    }

}
