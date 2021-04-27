package com.weather.weatherdataapi.corona;

public interface ICoronaItem {
    String getRegionName();

    Integer getNewLocalCaseCount();

    Integer getNewForeignCaseCount();
}
