package com.weather.weatherdataapi.exception.repository.redis;

public class InvalidLivingHealthRedisVOException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 LivingHealthRedisVO 입니다.";

    public InvalidLivingHealthRedisVOException() {
        super(LOG_PREFIX);
    }

    public InvalidLivingHealthRedisVOException(String message) {
        super(LOG_PREFIX + message);
    }
}
