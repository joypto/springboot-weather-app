package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "body", strict = false)
public class AirKoreaAirPollutionResponseBody {

    @ElementList(name = "items", required = false)
    private List<AirKoreaAirPollutionItem> itemList;

    /* XML에 포함되어 있는 값이지만 전혀 사용되지 않는 값입니다. 매핑하지 않습니다. */
//    @Element
//    private String numOfRows;
//
//    @Element
//    private String pageNo;
//
//    @Element
//    private String totalCount;

    public List<AirKoreaAirPollutionItem> getItemList() {
        return itemList;
    }
}
