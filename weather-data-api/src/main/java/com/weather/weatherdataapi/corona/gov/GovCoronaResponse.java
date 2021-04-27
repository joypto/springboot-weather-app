package com.weather.weatherdataapi.corona.gov;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "response", strict = false)
public class GovCoronaResponse {

    @Element(name = "header", required = false)
    private GovCoronaResponseHeader header;

    @Element(name = "body", required = false)
    private GovCoronaResponseBody body;
}
