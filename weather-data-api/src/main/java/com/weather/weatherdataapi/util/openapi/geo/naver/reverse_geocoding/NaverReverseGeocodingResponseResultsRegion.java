package com.weather.weatherdataapi.util.openapi.geo.naver.reverse_geocoding;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class NaverReverseGeocodingResponseResultsRegion {

    @SerializedName("area0")
    private NaverReverseGeocodingResponseResultsRegionArea area0;

    @SerializedName("area1")
    private NaverReverseGeocodingResponseResultsRegionArea area1;

    @SerializedName("area2")
    private NaverReverseGeocodingResponseResultsRegionArea area2;

    @SerializedName("area3")
    private NaverReverseGeocodingResponseResultsRegionArea area3;

    @SerializedName("area4")
    private NaverReverseGeocodingResponseResultsRegionArea area4;

}
