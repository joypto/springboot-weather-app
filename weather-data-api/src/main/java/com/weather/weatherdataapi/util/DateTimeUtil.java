package com.weather.weatherdataapi.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateTimeUtil {

    public final static String ZONE_NAME_ASIA_SEOUL = "Asia/Seoul";
    public final static ZoneId ZONE_ID_ASIA_SEOUL = ZoneId.of(ZONE_NAME_ASIA_SEOUL);
    public final static TimeZone TIMEZONE_ASIA_SEOUL = TimeZone.getTimeZone(ZONE_ID_ASIA_SEOUL);

    public static String getDateTimeString() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
    }

    public static String getDateString() {
        LocalDate date = LocalDate.now();
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static ZonedDateTime getSeoulNow() {
        return ZonedDateTime.now(ZONE_ID_ASIA_SEOUL);
    }

    public static ZonedDateTime convertSeoulZonedDateTime(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZONE_ID_ASIA_SEOUL);
    }

}
