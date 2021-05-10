package com.weather.weatherdataapi.exception.repository.redis;

public class InvalidWeatherDayRedisVOException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 WeatherDayRedisVO 입니다.";

    public InvalidWeatherDayRedisVOException() {
        super(LOG_PREFIX);
    }

    public InvalidWeatherDayRedisVOException(String message) {
        super(LOG_PREFIX + message);
    }
}
