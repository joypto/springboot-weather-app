package com.weather.weatherdataapi.air_pollution.airkorea;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.util.Optional;

@Slf4j
@Component
public class AirKoreaAirPollutionOpenApi {

    private String SERVICE_KEY = "iVwYPkC6bU1VAQicYcfS34fOnic5axhMluibhmVlWbQzkTP7YNapHzeMXMzwWzRjXYtTNk9shZRR+cveP6daGw==";

    private AirKoreaAirPollutionService service;

    public AirKoreaAirPollutionOpenApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(AirKoreaAirPollutionService.class);
    }

    public Optional<AirKoreaAirPollutionResponseBody> getResponse(String sidoName) {
        try {
            Call<AirKoreaAirPollutionResponse> call = service.getResponseCall(SERVICE_KEY, sidoName);
            AirKoreaAirPollutionResponse response = call.execute().body();

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
