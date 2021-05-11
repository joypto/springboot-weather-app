package com.weather.weatherdataapi.exception;

public class InvalidUserException extends NullPointerException {
    private static final String LOG_PREFIX = "유효하지 않은 Token 입니다.";

    public InvalidUserException() {
        super(LOG_PREFIX);
    }

    public InvalidUserException(String message) {
        super(LOG_PREFIX + message);
    }
}
