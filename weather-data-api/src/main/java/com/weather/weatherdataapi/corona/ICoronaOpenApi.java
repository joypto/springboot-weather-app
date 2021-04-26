package com.weather.weatherdataapi.corona;

public interface ICoronaOpenApi {
    String getRegion();

    Integer getNewLocalCaseCount();

    Integer getNewForeignCaseCount();
}
