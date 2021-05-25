package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "item", strict = false)
public class AirKoreaStationItem {

    @Element
    Double tm;

    @Element
    String addr;

    @Element
    String stationName;
}
