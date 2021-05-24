package com.weather.weatherdataapi.util.openapi.geo.naver.reverse_geocoding;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class NaverReverseGeocodingResponseResultsCode {

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("mappingId")
    private String mappingId;
}
