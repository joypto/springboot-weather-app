package com.weather.weatherdataapi.util.openapi.living;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "header")
public class LivingResponseHeader {
    @Element
    private String resultCode;

    @Element
    private String resultMsg;
}