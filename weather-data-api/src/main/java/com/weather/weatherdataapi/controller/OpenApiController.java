package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.dto.WeatherDataResponseDto;
import com.weather.weatherdataapi.model.entity.AirPollution;
import com.weather.weatherdataapi.model.entity.Corona;
import com.weather.weatherdataapi.model.entity.Region;
import com.weather.weatherdataapi.repository.RegionRepository;
import com.weather.weatherdataapi.service.AirPollutionService;
import com.weather.weatherdataapi.service.CoronaService;
import com.weather.weatherdataapi.service.LivingHealthWeatherService;
import com.weather.weatherdataapi.service.OpenApiService;
import com.weather.weatherdataapi.util.ReverseGeoCoding;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaStationUtil;
import com.weather.weatherdataapi.util.openapi.geo.kakao.KakaoGeoOpenApi;
import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponseDocument;
import com.weather.weatherdataapi.util.openapi.livinghealthweather.LivingHealthWeatherApiCall;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class OpenApiController {

    private final OpenApiService openApiService;
    private final LivingHealthWeatherService livingHealthWeatherService;
    private final RegionRepository regionRepository;
    private final LivingHealthWeatherApiCall livingHealthWeatherApiCall;
    private final ReverseGeoCoding reverseGeoCoding;
    private final CoronaService coronaService;
    private final AirPollutionService airPollutionService;
    private final AirKoreaStationUtil airKoreaStationUtil;
    private final KakaoGeoOpenApi kakaoGeoOpenApi;

    @GetMapping("/api/weather/data")
    public WeatherDataResponseDto getAllWeatherData(@RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude) throws ParseException, IOException {
        CoordinateDto coordinateDto = new CoordinateDto(longitude, latitude);
        ReverseGeocodingResponseDto address = reverseGeoCoding.reverseGeocoding(longitude, latitude);

        // 해당 시/구 주소를 가진 Region 객체 가져오기
        List<Region> regions = regionRepository.findByBigRegionAndSmallRegion(address.getBigRegion(), address.getSmallRegion());
        Region region = regions.get(0);

        // OPEN API 호출
        openApiService.callApi(coordinateDto, address, region);
        livingHealthWeatherApiCall.livingHealthWeatherApiCall(address, region);
        airPollutionService.getInfoByRegion(region);

        Corona corona = coronaService.getInfoByRegion(region);
        Corona coronaTotal = coronaService.getTotalInfo();

        // 점수반환 객체 생성
        ScoreResultResponseDto scoreResultResponseDto = new ScoreResultResponseDto();
        livingHealthWeatherService.livingHealthWthIdxConvertToScore(scoreResultResponseDto, region);
        scoreResultResponseDto.setCoronaResult(coronaService.calculateScore(coronaTotal));

        // 주간날씨 점수 반환
        openApiService.weekInfoConvertToScore(scoreResultResponseDto, region);

        WeatherDataResponseDto responseDto = new WeatherDataResponseDto(region, corona, coronaTotal);
        return responseDto;

    }

    @GetMapping("/api/corona/data")
    public Corona getCorona(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude) throws ParseException {
        ReverseGeocodingResponseDto reverseGeocodingResponseDto = reverseGeoCoding.reverseGeocoding(longitude, latitude);

        return coronaService.getInfoByRegion(reverseGeocodingResponseDto.getBigRegion());
    }

    @GetMapping("/api/air_pollution/data")
    public AirPollution getAirPollution(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude) throws ParseException {
        ReverseGeocodingResponseDto reverseGeocodingResponseDto = reverseGeoCoding.reverseGeocoding(longitude, latitude);

        Region region = regionRepository.findByBigRegionAndSmallRegion(reverseGeocodingResponseDto.getBigRegion(), reverseGeocodingResponseDto.getSmallRegion()).get(0);

        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegionName(region.getBigRegion(), region.getSmallRegion());

        AirPollution airPollution = airPollutionService.fetchAndStoreAirPollutionInfoUsingOpenApi(nearestStationName, region);

        return airPollution;
    }

    @GetMapping("/api/transcoord")
    public String getTranscoord(@RequestParam String x, @RequestParam String y) {
        try {
            KakaoGeoTranscoordResponseDocument document = kakaoGeoOpenApi.convertWGS84ToWTM(x, y);
            return document.getX() + "," + document.getY();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
