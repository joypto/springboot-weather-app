package com.weather.weatherdataapi.util.openapi.geo.kakao;

import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Component
public class KakaoGeoOpenApi {
    private final static String REST_API_KEY = "KakaoAK 00fe8b530605b8965d56229072117e02";

    private KakaoGeoService kakaoGeoService;

    public KakaoGeoOpenApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/v2/local/geo/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        kakaoGeoService = retrofit.create(KakaoGeoService.class);
    }

}
