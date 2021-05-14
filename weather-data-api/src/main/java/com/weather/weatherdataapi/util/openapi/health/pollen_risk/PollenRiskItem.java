package com.weather.weatherdataapi.util.openapi.health.pollen_risk;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "item", strict = false)
public class PollenRiskItem {

    @Element
    private String code;

    @Element
    private String areaNo;

    @Element
    private String date;

    @Element
    private String today;

    @Element
    private String tomorrow;

    @Element
    private String theDayAfterTomorrow;

}
