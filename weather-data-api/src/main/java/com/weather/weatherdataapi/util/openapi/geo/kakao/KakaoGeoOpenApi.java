package com.weather.weatherdataapi.util.openapi.geo.kakao;

import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponse;
import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponseDocument;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Component
public class KakaoGeoOpenApi {
    private final static String REST_API_KEY = "KakaoAK 00fe8b530605b8965d56229072117e02";

    private final KakaoGeoService service;

    public KakaoGeoOpenApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/v2/local/geo/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(KakaoGeoService.class);
    }

    public KakaoGeoTranscoordResponseDocument convertWGS84ToWTM(Double x, Double y) throws IOException {
        Call<KakaoGeoTranscoordResponse> kakaoGeoTranscoordResponseCall = service.generateTranscoordCall(REST_API_KEY, x.toString(), y.toString());
        KakaoGeoTranscoordResponse response = kakaoGeoTranscoordResponseCall.execute().body();

        return response.getDocuments().get(0);
    }

}
