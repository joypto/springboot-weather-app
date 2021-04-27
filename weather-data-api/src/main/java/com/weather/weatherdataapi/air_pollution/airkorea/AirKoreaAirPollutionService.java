package com.weather.weatherdataapi.air_pollution.airkorea;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AirKoreaAirPollutionService {
    @GET("getCtprvnRltmMesureDnsty?returnType=xml&ver=1.0")
    Call<AirKoreaAirPollutionResponse> getResponseCall(@Query("serviceKey") String serviceKey, @Query("sidoName") String sidoName);
}
