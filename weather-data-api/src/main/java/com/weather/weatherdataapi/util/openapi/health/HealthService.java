package com.weather.weatherdataapi.util.openapi.health;

import com.weather.weatherdataapi.util.openapi.health.asthma.AsthmaResponse;
import com.weather.weatherdataapi.util.openapi.health.food_poison.FoodPoisonResponse;
import com.weather.weatherdataapi.util.openapi.health.pollen_risk.PollenRiskResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HealthService {

    @GET("getAsthmaIdx?dataType=xml")
    Call<AsthmaResponse> generateAsthmaResponseCall(@Query("serviceKey") String serviceKey, @Query("areaNo") String areaNo, @Query("time") String time);

    @GET("getFoodPoisoningIdx?dataType=xml")
    Call<FoodPoisonResponse> generateFoodPoisonResponseCall(@Query("serviceKey") String serviceKey, @Query("areaNo") String areaNo, @Query("time") String time);

    @GET("getOakPollenRiskIdx?dataType=xml")
    Call<PollenRiskResponse> generateOakPollenRiskResponseCall(@Query("serviceKey") String serviceKey, @Query("areaNo") String areaNo, @Query("time") String time);

}
