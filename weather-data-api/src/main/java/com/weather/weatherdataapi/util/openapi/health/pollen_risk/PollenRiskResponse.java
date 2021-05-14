package com.weather.weatherdataapi.util.openapi.health.pollen_risk;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "response", strict = false)
public class PollenRiskResponse {

    @Element(name = "header")
    private PollenRiskResponseHeader header;

    @Element(name = "body", required = false)
    private PollenRiskResponseBody body;

}
