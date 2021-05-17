package com.weather.weatherdataapi.util.openapi.living.uv;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "header")
public class UvResponseHeader {
    @Element
    private String resultCode;

    @Element
    private String resultMsg;
}