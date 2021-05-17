package com.weather.weatherdataapi.util.openapi.health.food_poison;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "header")
public class FoodPoisonResponseHeader {
    @Element
    private String resultCode;

    @Element
    private String resultMsg;
}
