package com.weather.weatherdataapi.util.openapi.corona;

import java.util.Optional;

public interface ICoronaOpenApi {
    Optional<ICoronaInfo> getInfo() throws Exception;
}
