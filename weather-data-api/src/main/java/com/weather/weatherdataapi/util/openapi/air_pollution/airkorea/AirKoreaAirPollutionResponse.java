package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "response", strict = false)
public class AirKoreaAirPollutionResponse {

    @Element(name = "header")
    private AirKoreaAirPollutionResponseHeader header;

    @Element(name = "body", required = false)
    private AirKoreaAirPollutionResponseBody body;
}
