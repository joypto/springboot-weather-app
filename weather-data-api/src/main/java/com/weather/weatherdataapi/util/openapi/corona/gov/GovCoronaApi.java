package com.weather.weatherdataapi.util.openapi.corona.gov;

import com.weather.weatherdataapi.exception.FailedFetchException;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.openapi.OpenApiUtil;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaInfo;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaOpenApi;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class GovCoronaApi implements ICoronaOpenApi {

    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final GovCoronaService service;
    private final OpenApiUtil openApiUtil;

    public GovCoronaApi(OpenApiUtil openApiUtil) {
        this.openApiUtil = openApiUtil;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi.data.go.kr/openapi/service/rest/Covid19/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(GovCoronaService.class);
    }

    @Override
    public ICoronaInfo getInfo(LocalDate date) {
        try {
            String dateString = date.format(DATETIME_FORMATTER);

            Call<GovCoronaResponse> call = service.getResponseCall(openApiUtil.getDataGoKrApiKey(), dateString, dateString);
            Response<GovCoronaResponse> response = call.execute();
            GovCoronaResponse body = response.body();

            String resultCode = body.getHeader().getResultCode();
            if (resultCode.equals("00") == false)
                throw new FailedFetchException("원격 서버에서 온 에러 메세지 코드입니다. (" + resultCode + ")");

            else if (body.getBody().getItemList().size() == 0)
                throw new FailedFetchException("원격 서버에서 가져온 아이템 리스트가 비어있습니다.");

            return body.getBody();
        }
        // retrofit을 사용한 요청이 실패하였을 때의 예외 처리입니다.
        catch (IOException e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));

            throw new FailedFetchException("http 요청을 정상적으로 수행하지 못했습니다.");
        }
    }
}
