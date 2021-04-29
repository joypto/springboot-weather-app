package com.weather.weatherdataapi.util.openapi.geo.kakao;

import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface KakaoGeoService {
    @GET("transcoord.json?input_coord=WGS84&output_coord=WTM")
    Call<KakaoGeoTranscoordResponse> generateTranscoordCall(@Header("Authorization") String authentication, @Query("x") String x, @Query("y") String y);
}
