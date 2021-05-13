package com.weather.weatherdataapi.util.openapi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OpenApiUtil {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 생활기상지수와 보건기상지수 OpenApi 요청할 때 건네줄 유효한 time 문자열을 반환받습니다.
     * <p>
     * 생활지수 OpenApi는 당일 0시부터 6시 사이에 요청할 때,
     * 오늘의 데이터를 제공하지 않고 전일 18시 데이터를 제공합니다.
     * 하지만 전일 18시에 제공되는 응답에는 올바른 데이터가 담겨져있지 않기 때문에 사용할 수 없습니다.
     * 따라서 올바른 정보를 제공받기 위해서 0시 ~ 06시 사이의 요청을 전일 데이터 요청으로 치환한 뒤 요청할 필요가 있습니다.
     *
     * @param now 현재 시각입니다.
     */
    public static String getValidRequestTime(LocalDateTime now) {

        LocalDateTime dateTime = now.getHour() < 6 ? now.minusDays(1) : now;

        // 위에 서술했듯 18시에 제공되는 데이터는 올바른 데이터가 아닙니다.
        // 따라서 06시 데이터를 제공받도록 명시합니다.
        String timeText = dateTime.format(DATE_TIME_FORMATTER) + "06";

        return timeText;
    }

}
