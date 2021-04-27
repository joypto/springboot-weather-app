package com.weather.weatherdataapi.util.openapi.corona.gov;

import com.weather.weatherdataapi.util.openapi.corona.ICoronaInfo;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaItem;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "body", strict = false)
public class GovCoronaResponseBody implements ICoronaInfo {

    @ElementList(name = "items", required = false)
    private List<GovCoronaItem> itemList;

    /* XML에 포함되어 있는 값이지만 전혀 사용되지 않는 값입니다. 매핑하지 않습니다. */
//    @Element
//    private String numOfRows;
//
//    @Element
//    private String pageNo;
//
//    @Element
//    private String totalCount;

    @Override
    public List<ICoronaItem> getItemList() {
        return new ArrayList<>(itemList);
    }

}
