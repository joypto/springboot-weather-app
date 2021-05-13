package com.weather.weatherdataapi.util.openapi.health.asthma;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "header")
public class AsthmaResponseHeader {
    @Element
    private String resultCode;

    @Element
    private String resultMsg;
}
