package com.weather.weatherdataapi.corona.gov;

import com.weather.weatherdataapi.corona.ICoronaRegion;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GovCoronaRegion implements ICoronaRegion {

    private final String name;

    private final Integer newLocalCaseCount;

    private final Integer newForeignCaseCount;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getNewLocalCaseCount() {
        return this.newLocalCaseCount;
    }

    @Override
    public Integer getNewForeignCaseCount() {
        return this.newForeignCaseCount;
    }
}
