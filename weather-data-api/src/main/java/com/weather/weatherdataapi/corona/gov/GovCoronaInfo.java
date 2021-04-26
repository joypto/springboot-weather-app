package com.weather.weatherdataapi.corona.gov;

import com.weather.weatherdataapi.corona.ICoronaInfo;
import com.weather.weatherdataapi.corona.ICoronaRegion;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class GovCoronaInfo implements ICoronaInfo {

    private final LocalDateTime queryTime;

    private final List<ICoronaRegion> coronaRegionList;

    @Override
    public LocalDateTime getQueryTime() {
        return queryTime;
    }

    @Override
    public List<ICoronaRegion> getRegionList() {
        return coronaRegionList;
    }
}
