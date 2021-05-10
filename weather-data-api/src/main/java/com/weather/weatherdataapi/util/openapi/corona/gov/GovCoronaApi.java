package com.weather.weatherdataapi.util.openapi.corona.gov;

import com.weather.weatherdataapi.exception.FailedFetchException;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaInfo;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaOpenApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.io.IOException;

@Slf4j
@Component
public class GovCoronaApi implements ICoronaOpenApi {

    private String SERVICE_KEY = "iVwYPkC6bU1VAQicYcfS34fOnic5axhMluibhmVlWbQzkTP7YNapHzeMXMzwWzRjXYtTNk9shZRR+cveP6daGw==";

    private GovCoronaService service;

    public GovCoronaApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi.data.go.kr/openapi/service/rest/Covid19/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(GovCoronaService.class);
    }

    @Override
    public ICoronaInfo getInfo() {
        try {
            Call<GovCoronaResponse> call = service.getResponseCall(SERVICE_KEY);
            GovCoronaResponse response = call.execute().body();

            String resultCode = response.getHeader().getResultCode();
            if (resultCode.equals("00") == false)
                throw new FailedFetchException("원격 서버에서 온 에러 메세지 코드입니다. (" + resultCode + ")");

            return response.getBody();
        }
        // retrofit을 사용한 요청이 실패하였을 때의 예외 처리입니다.
        catch (IOException e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));

            throw new FailedFetchException("http 요청을 정상적으로 수행하지 못했습니다.");
        }
    }
}
