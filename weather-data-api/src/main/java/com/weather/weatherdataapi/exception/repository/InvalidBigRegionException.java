package com.weather.weatherdataapi.exception.repository;

public class InvalidBigRegionException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 BigRegion 입니다.";

    public InvalidBigRegionException() {
        super(LOG_PREFIX);
    }

    public InvalidBigRegionException(String message) {
        super(LOG_PREFIX + message);
    }
}
