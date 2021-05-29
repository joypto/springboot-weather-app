package com.weather.weatherdataapi.util.openapi.corona;

import java.time.LocalDate;

public interface ICoronaOpenApi {
    ICoronaInfo getInfo(LocalDate date) throws Exception;
}
