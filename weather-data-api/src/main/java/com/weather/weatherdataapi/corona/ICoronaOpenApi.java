package com.weather.weatherdataapi.corona;

import java.util.Optional;

public interface ICoronaOpenApi {
    Optional<ICoronaInfo> getInfo() throws Exception;
}
