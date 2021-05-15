package com.weather.weatherdataapi.util.openapi.living;

import com.weather.weatherdataapi.util.openapi.living.uv.UvResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LivingService {
    @GET("getUVIdx?dataType=xml")
    Call<UvResponse> generateResponseCall(@Query("serviceKey") String serviceKey, @Query("areaNo") String areaNo, @Query("time") String time);

}
