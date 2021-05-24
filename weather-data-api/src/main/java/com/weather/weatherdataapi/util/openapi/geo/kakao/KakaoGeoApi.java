package com.weather.weatherdataapi.util.openapi.geo.kakao;

import com.weather.weatherdataapi.util.openapi.OpenApiUtil;
import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponse;
import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponseDocument;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Component
public class KakaoGeoApi {

    private final KakaoGeoService service;

    private final OpenApiUtil openApiUtil;

    public KakaoGeoApi(OpenApiUtil openApiUtil) {
        this.openApiUtil = openApiUtil;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/v2/local/geo/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(KakaoGeoService.class);
    }

    public KakaoGeoTranscoordResponseDocument convertWGS84ToWTM(String x, String y) throws IOException {
        Call<KakaoGeoTranscoordResponse> kakaoGeoTranscoordResponseCall = service.generateTranscoordCall(openApiUtil.getKakaoApiKey(), x, y);
        KakaoGeoTranscoordResponse response = kakaoGeoTranscoordResponseCall.execute().body();

        return response.getDocuments().get(0);
    }

}
