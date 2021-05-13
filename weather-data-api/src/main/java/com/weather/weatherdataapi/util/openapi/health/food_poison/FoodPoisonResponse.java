package com.weather.weatherdataapi.util.openapi.health.food_poison;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "response", strict = false)
public class FoodPoisonResponse {

    @Element(name = "header")
    private FoodPoisonResponseHeader header;

    @Element(name = "body", required = false)
    private FoodPoisonResponseBody body;

}
