package com.weather.weatherdataapi.util.openapi.living;

import com.weather.weatherdataapi.exception.FailedFetchException;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.openapi.OpenApiUtil;
import com.weather.weatherdataapi.util.openapi.living.uv.UvItem;
import com.weather.weatherdataapi.util.openapi.living.uv.UvResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class LivingApi {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final LivingService service;
    private final OpenApiUtil openApiUtil;

    public LivingApi(OpenApiUtil openApiUtil) {
        this.openApiUtil = openApiUtil;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/1360000/LivingWthrIdxService01/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(LivingService.class);
    }

    public UvItem getUvResponse(String areaNo, LocalDate date) throws FailedFetchException {
        try {
            String requestTimeText = date.format(DATE_TIME_FORMATTER) + "06";

            Call<UvResponse> call = service.generateResponseCall(openApiUtil.getDataGoKrApiKey(), areaNo, requestTimeText);
            Response<UvResponse> execute;
            UvResponse response;

            try {
                execute = call.execute();
                response = execute.body();
            }
            // retrofit에서 exception이 발생할 때 실행됩니다.
            catch (Exception e) {
                throw new FailedFetchException();
            }

            if (response.getHeader().getResultCode().equals("00") == false) {
                throw new FailedFetchException("값을 정상적으로 조회하지 못했습니다.");
            } else if (response.getBody().getItemList().size() == 0) {
                throw new FailedFetchException("조회한 결과가 없습니다.");
            } else if (response.getBody().getItemList().get(0).getToday() == null) {
                throw new FailedFetchException("정상적인 today값을 응답받지 못했습니다.");
            }

            return response.getBody().getItemList().get(0);
        }
        // 값을 정상적으로 조회하지 못했을 때 실행됩니다.
        catch (FailedFetchException e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));

            throw e;
        }

    }

}
