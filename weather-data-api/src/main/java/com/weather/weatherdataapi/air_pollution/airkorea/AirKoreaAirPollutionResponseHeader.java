package com.weather.weatherdataapi.air_pollution.airkorea;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "header")
public class AirKoreaAirPollutionResponseHeader {
    @Element
    private String resultCode;

    @Element
    private String resultMsg;
}
