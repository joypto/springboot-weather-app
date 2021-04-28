package com.weather.weatherdataapi.util.openapi.corona.gov;

import com.weather.weatherdataapi.util.openapi.corona.ICoronaItem;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Root(name = "item", strict = false)
public class GovCoronaItem implements ICoronaItem {

    /*
     * 주로 사용되는 값에 대해서만 매핑했습니다.
     * 더 많은 값에 대한 설명은 문서를 참고해 주세요.
     * https://www.data.go.kr/data/15043378/openapi.do
     */

    @Element(name = "stdDay")
    private String dateString;

    @Element(name = "gubun")
    private String regionName;

    @Element(name = "localOccCnt")
    private Integer newLocalCaseCount;

    @Element(name = "overFlowCnt")
    private Integer newForeignCaseCount;

    @Override
    public String getRegionName() {
        return this.regionName;
    }

    @Override
    public LocalDate getDate() {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시", Locale.KOREA));
    }

    @Override
    public Integer getNewLocalCaseCount() {
        return this.newLocalCaseCount;
    }

    @Override
    public Integer getNewForeignCaseCount() {
        return this.newForeignCaseCount;
    }
}
