package com.weather.weatherdataapi.exception.repository.redis;

public class InvalidWeatherWeekRedisVOException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 WeatherWeekRedisVO 입니다.";

    public InvalidWeatherWeekRedisVOException() {
        super(LOG_PREFIX);
    }

    public InvalidWeatherWeekRedisVOException(String message) {
        super(LOG_PREFIX + message);
    }
}
