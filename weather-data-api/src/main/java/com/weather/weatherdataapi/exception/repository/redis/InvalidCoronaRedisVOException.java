package com.weather.weatherdataapi.exception.repository.redis;

public class InvalidCoronaRedisVOException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 CoronaRedisVO 입니다.";

    public InvalidCoronaRedisVOException() {
        super(LOG_PREFIX);
    }

    public InvalidCoronaRedisVOException(String message) {
        super(LOG_PREFIX + message);
    }
}
