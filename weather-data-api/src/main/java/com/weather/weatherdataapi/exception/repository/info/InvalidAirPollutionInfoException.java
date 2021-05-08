package com.weather.weatherdataapi.exception.repository.info;

public class InvalidAirPollutionInfoException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 AirPollutionInfo 입니다.";

    public InvalidAirPollutionInfoException() {
        super(LOG_PREFIX);
    }

    public InvalidAirPollutionInfoException(String message) {
        super(LOG_PREFIX + message);
    }
}
