package com.weather.weatherdataapi.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static String getDateTimeString() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
    }

    public static String getDateString() {
        LocalDate date = LocalDate.now();
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
