package com.weather.weatherdataapi.util.openapi.corona;

public interface ICoronaItem {
    String getRegionName();

    Integer getNewLocalCaseCount();

    Integer getNewForeignCaseCount();
}
