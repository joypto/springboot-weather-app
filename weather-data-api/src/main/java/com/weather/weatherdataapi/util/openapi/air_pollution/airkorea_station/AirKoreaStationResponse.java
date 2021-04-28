package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "response", strict = false)
public class AirKoreaStationResponse {

    @Element(name = "header")
    private AirKoreaStationResponseHeader header;

    @Element(name = "body", required = false)
    private AirKoreaStationResponseBody body;

}
