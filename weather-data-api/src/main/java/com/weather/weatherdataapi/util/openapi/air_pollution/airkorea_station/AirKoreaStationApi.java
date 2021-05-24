package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station;

import com.weather.weatherdataapi.exception.FailedFetchException;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.openapi.OpenApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.io.IOException;

@Slf4j
@Component
public class AirKoreaStationApi {

    private final AirKoreaStationService service;
    private final OpenApiUtil openApiUtil;

    public AirKoreaStationApi(OpenApiUtil openApiUtil) {
        this.openApiUtil = openApiUtil;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(AirKoreaStationService.class);
    }

    public AirKoreaStationItem getResponseItem(String tmX, String tmY) throws FailedFetchException {
        try {
            Call<AirKoreaStationResponse> call = service.getResponseCall(openApiUtil.getDataGoKrApiKey(), tmX, tmY);
            AirKoreaStationResponse response = call.execute().body();

            if (response.getHeader().getResultCode().equals("00") == false)
                throw new FailedFetchException("값을 정상적으로 조회하지 못했습니다.");

            return response.getBody().getItemList().get(0);
        }
        // 값을 정상적으로 조회하지 못했을 때 실행됩니다.
        catch (FailedFetchException e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));

            throw e;
        }
        // retrofit에서 exception이 발생할 때 실행됩니다.
        catch (IOException e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));

            throw new FailedFetchException();
        }

    }
}
