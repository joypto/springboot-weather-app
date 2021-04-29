package com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class KakaoGeoTranscoordResponseMeta {
    @SerializedName("total_count")
    private Integer totalCount;
}
