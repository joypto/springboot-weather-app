package com.weather.weatherdataapi.corona;

import java.time.LocalDateTime;
import java.util.List;

public interface ICoronaInfo {
    LocalDateTime getQueryTime();

    List<ICoronaRegion> getRegionList();
}
