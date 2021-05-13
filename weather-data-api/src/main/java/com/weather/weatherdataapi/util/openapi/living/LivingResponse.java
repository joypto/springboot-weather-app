package com.weather.weatherdataapi.util.openapi.living;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "response", strict = false)
public class LivingResponse {

    @Element(name = "header")
    private LivingResponseHeader header;

    @Element(name = "body", required = false)
    private LivingResponseBody body;
}
