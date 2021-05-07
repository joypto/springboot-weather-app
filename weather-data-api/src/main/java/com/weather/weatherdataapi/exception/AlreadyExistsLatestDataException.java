package com.weather.weatherdataapi.exception;

public class AlreadyExistsLatestDataException extends RuntimeException {
    private static final String LOG_PREFIX = "OpenApi를 사용하여 원격 서버에서 가져온 정보와 일치하거나 그보다 더 최신 정보가 이미 저장되어 있습니다.";

    public AlreadyExistsLatestDataException() {
        super(LOG_PREFIX);
    }

    public AlreadyExistsLatestDataException(String message) {
        super(LOG_PREFIX + message);
    }
}
