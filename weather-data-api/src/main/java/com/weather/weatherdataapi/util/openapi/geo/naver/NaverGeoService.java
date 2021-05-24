package com.weather.weatherdataapi.util.openapi.geo.naver;

import com.weather.weatherdataapi.util.openapi.geo.naver.reverse_geocoding.NaverReverseGeocodingResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NaverGeoService {
    @GET("map-reversegeocode/v2/gc?output=json&orders=admcode")
    Call<NaverReverseGeocodingResponse> generateReverseGeocodingCall(@Header("X-NCP-APIGW-API-KEY-ID") String apiKeyId, @Header("X-NCP-APIGW-API-KEY") String apiKey, @Query("sourcecrs") String sourceCrs, @Query("coords") String coords);
}
