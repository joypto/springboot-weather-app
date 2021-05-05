package com.weather.weatherdataapi.exception;

public class FailedFetchException extends RuntimeException {
    private static final String LOG_PREFIX = "OpenApi를 사용하여 원격 서버에서 정보를 가져오는 데 실패하였습니다. ";

    public FailedFetchException() {
        super(LOG_PREFIX);
    }

    public FailedFetchException(String message) {
        super(LOG_PREFIX + message);
    }
}
