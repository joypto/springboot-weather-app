package com.weather.weatherdataapi.util.openapi.living;

import com.weather.weatherdataapi.exception.FailedFetchException;
import com.weather.weatherdataapi.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class LivingApi {

    private final LivingService service;
    private final String SERVICE_KEY = "iVwYPkC6bU1VAQicYcfS34fOnic5axhMluibhmVlWbQzkTP7YNapHzeMXMzwWzRjXYtTNk9shZRR+cveP6daGw==";
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public LivingApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/1360000/LivingWthrIdxService01/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(LivingService.class);
    }

    public LivingItem getResponse(String areaNo, LocalDateTime now) throws FailedFetchException {
        try {
            // 생활지수 OpenApi는 당일 0시부터 6시 사이에 요청할 때,
            // 오늘의 데이터를 제공하지 않고 전일 18시 데이터를 제공합니다.
            // 하지만 전일 18시에 제공되는 응답에는 올바른 데이터가 담겨져있지 않기 때문에 사용할 수 없습니다.
            // 따라서 올바른 정보를 제공받기 위해서 0시 ~ 06시 사이의 요청을 전일 데이터 요청으로 치환한 뒤 요청할 필요가 있습니다.
            LocalDateTime requestDateTime = now.getHour() < 6 ? now.minusDays(1) : now;

            // 위에 서술했듯 18시에 제공되는 데이터는 올바른 데이터가 아닙니다.
            // 따라서 06시 데이터를 제공받도록 명시합니다.
            String requestTimeText = requestDateTime.format(DATE_TIME_FORMATTER) + "06";

            Call<LivingResponse> call = service.generateResponseCall(SERVICE_KEY, areaNo, requestTimeText);
            Response<LivingResponse> execute = call.execute();
            LivingResponse response = execute.body();

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

            throw new FailedFetchException("원격 서버에서 응답받은 xml데이터가 LivingResponse객체에 매핑될 수 없습니다.");
        }

    }

}
