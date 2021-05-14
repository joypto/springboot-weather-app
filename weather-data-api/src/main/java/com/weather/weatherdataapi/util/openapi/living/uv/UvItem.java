package com.weather.weatherdataapi.util.openapi.living.uv;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "item", strict = false)
public class UvItem {

    @Element
    private String code;

    @Element
    private String areaNo;

    @Element
    private String date;

    @Element
    private Integer today;

    @Element
    private Integer tomorrow;

    @Element
    private Integer theDayAfterTomorrow;

}
