package com.weather.weatherdataapi.exception.repository.info;

public class InvalidWeatherWeekInfoException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 WeatherWeekInfo 입니다. ";

    public InvalidWeatherWeekInfoException() {
        super(LOG_PREFIX);
    }

    public InvalidWeatherWeekInfoException(String message) {
        super(LOG_PREFIX + message);
    }

}
