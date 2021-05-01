package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.util.Optional;

@Slf4j
@Component
public class AirKoreaAirPollutionApi {

    private String SERVICE_KEY = "iVwYPkC6bU1VAQicYcfS34fOnic5axhMluibhmVlWbQzkTP7YNapHzeMXMzwWzRjXYtTNk9shZRR+cveP6daGw==";

    private final AirKoreaAirPollutionService service;

    public AirKoreaAirPollutionApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(AirKoreaAirPollutionService.class);
    }

    public Optional<AirKoreaAirPollutionItem> getResponseByStationName(String stationName) {
        try {
            Call<AirKoreaAirPollutionResponse> call = service.getResponseByStationName(SERVICE_KEY, stationName);
            AirKoreaAirPollutionResponse response = call.execute().body();

            if (response.getHeader().getResultCode().equals("00") == false) {
                throw new Exception("값을 정상적으로 조회하지 못했습니다.");
            } else if (response.getBody().getItemList().size() == 0) {
                throw new Exception("조회해온 값이 없습니다.");
            }

            return Optional.of(response.getBody().getItemList().get(0));
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();

            return Optional.empty();
        }
    }
}
