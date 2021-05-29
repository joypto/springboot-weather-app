package com.weather.weatherdataapi.util.openapi.corona.gov;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GovCoronaService {
    @GET("getCovid19SidoInfStateJson")
    Call<GovCoronaResponse> getResponseCall(@Query("serviceKey") String serviceKey, @Query("startCreateDt") String startCreateDt, @Query("endCreateDt") String endCreateDt);
}
