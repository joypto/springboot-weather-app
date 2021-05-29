package com.weather.weatherdataapi.util.openapi.geo.naver.reverse_geocoding;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class NaverReverseGeocodingResponseStatus {
    @SerializedName("code")
    private Integer code;

    @SerializedName("name")
    private String name;

    @SerializedName("message")
    private String message;
}
