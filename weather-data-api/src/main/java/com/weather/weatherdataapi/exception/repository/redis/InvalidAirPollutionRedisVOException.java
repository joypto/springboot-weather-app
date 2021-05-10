package com.weather.weatherdataapi.exception.repository.redis;

public class InvalidAirPollutionRedisVOException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 AirPollutionRedisVO 입니다.";

    public InvalidAirPollutionRedisVOException() {
        super(LOG_PREFIX);
    }

    public InvalidAirPollutionRedisVOException(String message) {
        super(LOG_PREFIX + message);
    }
}
