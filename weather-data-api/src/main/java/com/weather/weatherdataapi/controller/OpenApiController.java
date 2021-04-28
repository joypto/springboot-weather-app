package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoronaRequestDto;
import com.weather.weatherdataapi.model.dto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.WeatherDataRequestDto;
import com.weather.weatherdataapi.model.entity.Corona;
import com.weather.weatherdataapi.model.entity.region.Region;
import com.weather.weatherdataapi.repository.region.RegionRepository;
import com.weather.weatherdataapi.service.CoronaService;
import com.weather.weatherdataapi.service.OpenApiService;
import com.weather.weatherdataapi.util.ReverseGeoCoding;
import com.weather.weatherdataapi.util.openapi.livinghealthweather.LivingHealthWeatherApiCall;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class OpenApiController {

    private final OpenApiService openApiService;
    private final RegionRepository regionRepository;
    private final LivingHealthWeatherApiCall livingHealthWeatherApiCall;
    private final ReverseGeoCoding reverseGeoCoding;
    private final CoronaService coronaService;

    @GetMapping("/api/weather/data")
    public ReverseGeocodingResponseDto getAllWeatherData(@RequestBody WeatherDataRequestDto weatherDataRequestDto) throws ParseException, IOException {
        ReverseGeocodingResponseDto address = reverseGeoCoding.reverseGeocoding(weatherDataRequestDto.getLongitude(), weatherDataRequestDto.getLatitude());

        // 해당 시/구 주소를 가진 Region 객체 가져오기
        List<Region> regions = regionRepository.findByBigRegionAndSmallRegion(address.getBigRegion(), address.getSmallRegion());
        Region region = regions.get(0);

        // OPEN API 호출
        openApiService.callApi(weatherDataRequestDto, address, region);
        livingHealthWeatherApiCall.livingHealthWeatherApiCall(address, region);


        return address;
    }

    @GetMapping("/api/corona/data")
    public Corona getCorona(@RequestBody CoronaRequestDto requestDto) throws ParseException {
        List<Corona> coronaList = coronaService.fetchAndStoreCoronaInfoUsingOpenApi();

        ReverseGeocodingResponseDto reverseGeocodingResponseDto = reverseGeoCoding.reverseGeocoding(requestDto.getLongitude(), requestDto.getLatitude());
        String areaAlias = reverseGeocodingResponseDto.getAlias();

        for (Corona corona : coronaList) {
            if (corona.getSido_name().equals(areaAlias)) {
                return corona;
            }
        }

        return null;
    }

}
