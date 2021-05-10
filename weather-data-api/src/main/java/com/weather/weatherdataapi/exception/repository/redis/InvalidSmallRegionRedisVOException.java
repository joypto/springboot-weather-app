package com.weather.weatherdataapi.exception.repository.redis;

public class InvalidSmallRegionRedisVOException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 SmallRegionRedisVO 입니다.";

    public InvalidSmallRegionRedisVOException() {
        super(LOG_PREFIX);
    }

    public InvalidSmallRegionRedisVOException(String message) {
        super(LOG_PREFIX + message);
    }
}
