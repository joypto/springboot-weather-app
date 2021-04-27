package com.weather.weatherdataapi.util.openapi.corona.gov;

import com.weather.weatherdataapi.util.openapi.corona.ICoronaInfo;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaOpenApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.util.Optional;

@Slf4j
@Component
public class GovCoronaOpenApi implements ICoronaOpenApi {

    private String SERVICE_KEY = "iVwYPkC6bU1VAQicYcfS34fOnic5axhMluibhmVlWbQzkTP7YNapHzeMXMzwWzRjXYtTNk9shZRR+cveP6daGw==";

    private GovCoronaService service;

    public GovCoronaOpenApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi.data.go.kr/openapi/service/rest/Covid19/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(GovCoronaService.class);
    }

    @Override
    public Optional<ICoronaInfo> getInfo() {
        try {
            Call<GovCoronaResponse> call = service.getResponseCall(SERVICE_KEY);
            GovCoronaResponse response = call.execute().body();

            if (response.getHeader().getResultCode().equals("00") == false) {
                throw new Exception("값을 정상적으로 조회하지 못했습니다.");
            }

            return Optional.of(response.getBody());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();

            return Optional.empty();
        }

    }
}
