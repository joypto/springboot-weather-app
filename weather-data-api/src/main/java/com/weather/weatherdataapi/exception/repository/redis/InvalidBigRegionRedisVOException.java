package com.weather.weatherdataapi.exception.repository.redis;

public class InvalidBigRegionRedisVOException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 BigRegionRedisVO 입니다.";

    public InvalidBigRegionRedisVOException() {
        super(LOG_PREFIX);
    }

    public InvalidBigRegionRedisVOException(String message) {
        super(LOG_PREFIX + message);
    }
}
