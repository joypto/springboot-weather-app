package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "item", strict = false)
public class AirKoreaAirPollutionItem {

    /**
     * 측정일시
     */
    @Element(required = false)
    private String dataTime;

    /**
     * 미세먼지(PM10) 농도 (단위 : ㎍/㎥)
     */
    @Element(required = false)
    private String pm10Value;

    /**
     * 미세먼지(PM10) 24시간 등급자료
     */
    @Element(required = false)
    private String pm10Grade;

    /**
     * 미세먼지(PM2.5) 농도 (단위 : ㎍/㎥)
     */
    @Element(required = false)
    private String pm25Value;

    /**
     * 미세먼지(PM2.5) 24시간 등급자료
     */
    @Element(required = false)
    private String pm25Grade;


    /**
     * 일산화탄소 농도(단위 : ppm)
     */
    @Element(required = false)
    private String coValue;

    /**
     * 일산화탄소 지수
     */
    @Element(required = false)
    private String coGrade;

    /**
     * 이산화질소 농도(단위 : ppm)
     */
    @Element(required = false)
    private String no2Value;

    /**
     * 이산화질소 지수
     */
    @Element(required = false)
    private String no2Grade;

    /**
     * 오존 농도(단위 : ppm)
     */
    @Element(required = false)
    private String o3Value;

    /**
     * 오존 지수
     */
    @Element(required = false)
    private String o3Grade;

    /**
     * 아황산가스 지수
     */
    @Element(required = false)
    private String so2Value;

    /**
     * 아황산가스 농도
     */
    @Element(required = false)
    private String so2Grade;

    /**
     * 통합대기환경수치
     */
    @Element(required = false)
    private String khaiValue;

    /**
     * 통합대기환경지수
     */
    @Element(required = false)
    private String khaiGrade;

//    /**
//     * 측정자료 상태정보(점검및교정,장비점검,자료이상,통신장애)
//     */
//    @Element(required = false)
//    private String coFlag;
//
//    @Element(required = false)
//    private String pm25Flag;
//
//    @Element(required = false)
//    private String pm10Flag;
//
//    @Element(required = false)
//    private String no2Flag;
//
//    @Element(required = false)
//    private String o3Flag;
//
//    @Element(required = false)
//    private String so2Flag;

}
