package com.weather.weatherdataapi.util.openapi.geo.naver;

import com.weather.weatherdataapi.exception.FailedFetchException;
import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.openapi.OpenApiUtil;
import com.weather.weatherdataapi.util.openapi.geo.naver.reverse_geocoding.NaverReverseGeocodingResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Slf4j
@Component
public class NaverGeoApi {

    private static final String SOURCE_CRS = "epsg:4326";

    private final NaverGeoService service;
    private final OpenApiUtil openApiUtil;

    public NaverGeoApi(OpenApiUtil openApiUtil) {
        this.openApiUtil = openApiUtil;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(NaverGeoService.class);
    }

    public RegionDto reverseGeocoding(CoordinateDto coordinateDto) {

        try {
            String coords = coordinateDto.getLongitude() + "," + coordinateDto.getLatitude();

            Call<NaverReverseGeocodingResponse> naverReverseGeocodingResponseCall = service.generateReverseGeocodingCall(openApiUtil.getNaverApiKeyId(), openApiUtil.getNaverApiKey(), SOURCE_CRS, coords);
            Response<NaverReverseGeocodingResponse> execute = naverReverseGeocodingResponseCall.execute();
            NaverReverseGeocodingResponse body = execute.body();

            if (body == null)
                throw new FailedFetchException();

            if (body.getStatus().getCode() != 0)
                throw new FailedFetchException("원격 서버에서 온 에러 메세지 코드입니다. (" + body.getStatus().getCode() + ")");

            else if (body.getResults().size() == 0)
                throw new FailedFetchException("원격 서버에서 가져온 아이템 리스트가 비어있습니다.");

            RegionDto regionDto = new RegionDto(
                    body.getResults().get(0).getRegion().getArea1().getName(),
                    body.getResults().get(0).getRegion().getArea2().getName()
            );

            return regionDto;
        }
        // retrofit을 사용한 요청이 실패하였을 때의 예외 처리입니다.
        catch (IOException e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));

            throw new FailedFetchException("http 요청을 정상적으로 수행하지 못했습니다.");
        }

    }

}
