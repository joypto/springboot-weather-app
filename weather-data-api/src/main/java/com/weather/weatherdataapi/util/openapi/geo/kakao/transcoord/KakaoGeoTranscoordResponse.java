package com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class KakaoGeoTranscoordResponse {
    @SerializedName("meta")
    private KakaoGeoTranscoordResponseMeta meta;

    @SerializedName("documents")
    private List<KakaoGeoTranscoordResponseDocument> documents;

}