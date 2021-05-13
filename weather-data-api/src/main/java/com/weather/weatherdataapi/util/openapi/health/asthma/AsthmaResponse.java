package com.weather.weatherdataapi.util.openapi.health.asthma;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "response", strict = false)
public class AsthmaResponse {

    @Element(name = "header")
    private AsthmaResponseHeader header;

    @Element(name = "body", required = false)
    private AsthmaResponseBody body;

}
