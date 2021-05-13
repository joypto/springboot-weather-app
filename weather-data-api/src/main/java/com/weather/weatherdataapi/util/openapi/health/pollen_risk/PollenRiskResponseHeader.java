package com.weather.weatherdataapi.util.openapi.health.pollen_risk;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "header")
public class PollenRiskResponseHeader {
    @Element
    private String resultCode;

    @Element
    private String resultMsg;
}
