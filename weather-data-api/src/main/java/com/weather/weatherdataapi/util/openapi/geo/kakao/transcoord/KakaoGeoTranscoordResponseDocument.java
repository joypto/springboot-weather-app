package com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class KakaoGeoTranscoordResponseDocument {
    @SerializedName("x")
    private String x;

    @SerializedName("y")
    private String y;
}
