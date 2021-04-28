package com.weather.weatherdataapi.util.openapi.corona;

import java.time.LocalDate;

public interface ICoronaItem {
    String getRegionName();

    LocalDate getDate();

    Integer getNewLocalCaseCount();

    Integer getNewForeignCaseCount();
}
