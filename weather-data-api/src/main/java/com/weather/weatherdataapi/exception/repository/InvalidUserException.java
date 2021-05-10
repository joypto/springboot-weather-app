package com.weather.weatherdataapi.exception.repository;

public class InvalidUserException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 User 입니다.";

    public InvalidUserException() {
        super(LOG_PREFIX);
    }

    public InvalidUserException(String message) {
        super(LOG_PREFIX + message);
    }
}
