package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "header")
public class AirKoreaStationResponseHeader {

    @Element
    private String resultCode;

    @Element
    private String resultMsg;

}
