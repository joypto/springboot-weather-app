package com.weather.weatherdataapi.util.openapi.living;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "item", strict = false)
public class LivingItem {

    @Element(required = false)
    private String code;

    @Element(required = false)
    private String areaNo;

    @Element(required = false)
    private String date;

    @Element(required = false)
    private Integer today;

    @Element(required = false)
    private Integer tomorrow;

    @Element(required = false)
    private Integer theDayAfterTomorrow;

}
