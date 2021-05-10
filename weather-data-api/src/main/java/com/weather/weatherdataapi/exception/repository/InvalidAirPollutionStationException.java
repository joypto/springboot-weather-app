package com.weather.weatherdataapi.exception.repository;

public class InvalidAirPollutionStationException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 AirPollutionStation 입니다.";

    public InvalidAirPollutionStationException() {
        super(LOG_PREFIX);
    }

    public InvalidAirPollutionStationException(String message) {
        super(LOG_PREFIX + message);
    }
}
