package com.weather.weatherdataapi.util.openapi.geo.naver.reverse_geocoding;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class NaverReverseGeocodingResponseResult {
    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private NaverReverseGeocodingResponseResultsCode code;

    @SerializedName("region")
    private NaverReverseGeocodingResponseResultsRegion region;

}
