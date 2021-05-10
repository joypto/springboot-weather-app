package com.weather.weatherdataapi.exception.repository.info;

public class InvalidWeatherDayInfoException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 WeatherDayInfo 입니다.";

    public InvalidWeatherDayInfoException() {
        super(LOG_PREFIX);
    }

    public InvalidWeatherDayInfoException(String message) {
        super(LOG_PREFIX + message);
    }
}
