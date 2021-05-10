package com.weather.weatherdataapi.exception;

public class InvalidTokenException extends NullPointerException {
    private static final String LOG_PREFIX = "유효하지 않은 Token 입니다.";

    public InvalidTokenException() {
        super(LOG_PREFIX);
    }

    public InvalidTokenException(String message) {
        super(LOG_PREFIX + message);
    }
}
