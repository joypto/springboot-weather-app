package com.weather.weatherdataapi.corona;

public interface ICoronaRegion {
    String getName();

    Integer getNewLocalCaseCount();

    Integer getNewForeignCaseCount();
}
