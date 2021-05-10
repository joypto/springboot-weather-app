package com.weather.weatherdataapi.exception.repository.info;

public class InvalidLivingHealthInfoException extends RuntimeException {
    private static final String LOG_PREFIX = "유효하지 않은 LivingHealthInfo 입니다.";

    public InvalidLivingHealthInfoException() {
        super(LOG_PREFIX);
    }

    public InvalidLivingHealthInfoException(String message) {
        super(LOG_PREFIX + message);
    }
}
