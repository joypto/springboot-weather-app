package com.weather.weatherdataapi.air_pollution.airkorea;

import lombok.Getter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Root(name = "item", strict = false)
public class AirKoreaAirPollutionItem {

    /**
     * 측정일시
     */
    @Element
    private String dataTime;

    /**
     * 시·도 이름
     */
    @Element
    private String sidoName;

    /**
     * 측정소명
     */
    @Element
    private String stationName;

    /**
     * 미세먼지(PM10) 농도 (단위 : ㎍/㎥)
     */
    @Element
    private String pm10Value;

    /**
     * 미세먼지(PM10) 24시간 등급자료
     */
    @Element
    private String pm10Grade;

    /**
     * 미세먼지(PM2.5) 농도 (단위 : ㎍/㎥)
     */
    @Element
    private String pm25Value;

    /**
     * 미세먼지(PM2.5) 24시간 등급자료
     */
    @Element
    private String pm25Grade;


    /**
     * 일산화탄소 농도(단위 : ppm)
     */
    @Element
    private String coValue;

    /**
     * 일산화탄소 지수
     */
    @Element
    private String coGrade;

    /**
     * 이산화질소 농도(단위 : ppm)
     */
    @Element
    private String no2Value;

    /**
     * 이산화질소 지수
     */
    @Element
    private String no2Grade;

    /**
     * 오존 농도(단위 : ppm)
     */
    @Element
    private String o3Value;

    /**
     * 오존 지수
     */
    @Element
    private String o3Grade;

    /**
     * 아황산가스 지수
     */
    @Element
    private String so2Value;

    /**
     * 아황산가스 농도
     */
    @Element
    private String so2Grade;

    /**
     * 통합대기환경수치
     */
    @Element
    private String khaiValue;

    /**
     * 통합대기환경지수
     */
    @Element
    private String khaiGrade;

//    /**
//     * 측정자료 상태정보(점검및교정,장비점검,자료이상,통신장애)
//     */
//    @Element
//    private String coFlag;
//
//    @Element
//    private String pm25Flag;
//
//    @Element
//    private String pm10Flag;
//
//    @Element
//    private String no2Flag;
//
//    @Element
//    private String o3Flag;
//
//    @Element
//    private String so2Flag;

}
