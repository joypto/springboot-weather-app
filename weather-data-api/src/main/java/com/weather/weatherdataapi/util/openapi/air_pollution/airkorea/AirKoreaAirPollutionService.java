package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AirKoreaAirPollutionService {
    @GET("getMsrstnAcctoRltmMesureDnsty?ver=1.0&returnType=xml&dataTerm=DAILY&pageNo=1&numOfRows=1")
    Call<AirKoreaAirPollutionResponse> getResponseByStationName(@Query("serviceKey") String serviceKey, @Query("stationName") String stationName);
}
