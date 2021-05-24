package com.weather.weatherdataapi.util.openapi.geo.naver.reverse_geocoding;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class NaverReverseGeocodingResponse {

    @SerializedName("status")
    private NaverReverseGeocodingResponseStatus status;

    @SerializedName("results")
    private List<NaverReverseGeocodingResponseResult> results;
}
