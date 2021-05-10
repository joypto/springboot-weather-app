package com.weather.weatherdataapi.exception.repository.info;

public class InvalidCoronaInfoException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 CoronaInfo 입니다.";

    public InvalidCoronaInfoException() {
        super(LOG_PREFIX);
    }

    public InvalidCoronaInfoException(String message) {
        super(LOG_PREFIX + message);
    }
}
