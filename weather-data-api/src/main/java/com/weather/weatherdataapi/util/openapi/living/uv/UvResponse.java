package com.weather.weatherdataapi.util.openapi.living.uv;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "response", strict = false)
public class UvResponse {

    @Element(name = "header")
    private UvResponseHeader header;

    @Element(name = "body", required = false)
    private UvResponseBody body;
}
