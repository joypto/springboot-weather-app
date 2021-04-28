package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station;

import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AirKoreaStationService {

    @GET("getNearbyMsrstnList?returnType=xml")
    Call<AirKoreaStationResponse> getResponseCall(@Query("serviceKey") String serviceKey, @Query("tmX") String tmX, @Query("tmY") String tmY);
}
