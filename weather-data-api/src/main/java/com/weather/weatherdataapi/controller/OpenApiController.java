package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.AirPollutionRequestDto;
import com.weather.weatherdataapi.model.dto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.WeatherDataRequestDto;
import com.weather.weatherdataapi.model.entity.AirPollution;
import com.weather.weatherdataapi.model.entity.region.Region;
import com.weather.weatherdataapi.repository.region.RegionRepository;
import com.weather.weatherdataapi.service.AirPollutionService;
import com.weather.weatherdataapi.service.CoronaService;
import com.weather.weatherdataapi.service.OpenApiService;
import com.weather.weatherdataapi.util.ReverseGeoCoding;
import com.weather.weatherdataapi.util.openapi.livinghealthweather.LivingHealthWeatherApiCall;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class OpenApiController {

    private final OpenApiService openApiService;
    private final RegionRepository regionRepository;
    private final LivingHealthWeatherApiCall livingHealthWeatherApiCall;
    private final ReverseGeoCoding reverseGeoCoding;
    private final CoronaService coronaService;
    private final AirPollutionService airPollutionService;

    @GetMapping("/api/weather/data")
    public Region getAllWeatherData(@RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude, WeatherDataRequestDto weatherDataRequestDto) throws ParseException, IOException {
        weatherDataRequestDto.setLatitude(latitude);
        weatherDataRequestDto.setLongitude(longitude);
        ReverseGeocodingResponseDto address = reverseGeoCoding.reverseGeocoding(weatherDataRequestDto.getLongitude(), weatherDataRequestDto.getLatitude());

        // 해당 시/구 주소를 가진 Region 객체 가져오기
        List<Region> regions = regionRepository.findByBigRegionAndSmallRegion(address.getBigRegion(), address.getSmallRegion());
        Region region = regions.get(0);

        // OPEN API 호출
        openApiService.callApi(weatherDataRequestDto, address, region);
        livingHealthWeatherApiCall.livingHealthWeatherApiCall(address, region);

        return region;
    }

//    @GetMapping("/api/corona/data")
//    public Corona getCorona(@RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude, CoronaRequestDto requestDto) throws ParseException {
//        coronaService.fetchAndStoreCoronaInfoUsingOpenApi();
//
//        ReverseGeocodingResponseDto reverseGeocodingResponseDto = reverseGeoCoding.reverseGeocoding(requestDto.getLongitude(), requestDto.getLatitude());
//        String areaAlias = reverseGeocodingResponseDto.getAlias();
//
//        for (Corona corona : coronaList) {
//            if (corona.getSido_name().equals(areaAlias)) {
//                return corona;
//            }
//        }
//
//        return null;
//    }

    @GetMapping("/api/air_pollution/data")
    public AirPollution getAirPollution(@RequestBody AirPollutionRequestDto requestDto) throws ParseException {
        ReverseGeocodingResponseDto reverseGeocodingResponseDto = reverseGeoCoding.reverseGeocoding(requestDto.getLongitude(), requestDto.getLatitude());

        String stationName = airPollutionService.getStationNameUsingCoords(requestDto.getTmX(), requestDto.getTmY());

        AirPollution airPollution = airPollutionService.fetchAndStoreAirPollutionInfoUsingOpenApi(stationName, reverseGeocodingResponseDto.getBigRegion(), reverseGeocodingResponseDto.getSmallRegion());

        return airPollution;
    }
}
