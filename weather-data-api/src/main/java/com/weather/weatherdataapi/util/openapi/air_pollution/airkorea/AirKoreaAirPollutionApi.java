package com.weather.weatherdataapi.util.openapi.air_pollution.airkorea;

import com.weather.weatherdataapi.exception.FailedFetchException;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.openapi.OpenApiUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.io.IOException;

@Slf4j
@Component
public class AirKoreaAirPollutionApi {

    private final AirKoreaAirPollutionService service;
    private final OpenApiUtil openApiUtil;

    public AirKoreaAirPollutionApi(OpenApiUtil openApiUtil) {
        this.openApiUtil = openApiUtil;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(AirKoreaAirPollutionService.class);
    }

    public AirKoreaAirPollutionItem getResponseByStationName(String stationName) throws FailedFetchException {
        try {
            Call<AirKoreaAirPollutionResponse> call = service.getResponseByStationName(openApiUtil.getDataGoKrApiKey(), stationName);
            Response<AirKoreaAirPollutionResponse> execute = call.execute();
            AirKoreaAirPollutionResponse response = execute.body();

            if (response.getHeader().getResultCode().equals("00") == false) {
                throw new FailedFetchException("값을 정상적으로 조회하지 못했습니다.");
            } else if (response.getBody().getItemList().size() == 0) {
                throw new FailedFetchException("조회한 결과가 없습니다.");
            }

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

            throw new FailedFetchException("원격 서버에서 응답받은 xml데이터가 AirPollutionResponse객체에 매핑될 수 없습니다.");
        }

    }
}
