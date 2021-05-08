package com.weather.weatherdataapi.exception.repository;

public class InvalidSmallRegionException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 SmallRegion 입니다.";

    public InvalidSmallRegionException() {
        super(LOG_PREFIX);
    }

    public InvalidSmallRegionException(String message) {
        super(LOG_PREFIX + message);
    }
}
