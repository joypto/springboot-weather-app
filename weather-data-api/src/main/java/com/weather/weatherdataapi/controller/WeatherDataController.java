package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.*;
import com.weather.weatherdataapi.model.dto.requestdto.ScoreRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.WeatherDataResponseDto;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import com.weather.weatherdataapi.model.entity.Region;
import com.weather.weatherdataapi.repository.RegionRepository;
import com.weather.weatherdataapi.service.*;
import com.weather.weatherdataapi.util.openapi.ReverseGeoCoding;
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
public class WeatherDataController {

    private final WeatherService openApiService;
    private final LivingHealthService livingHealthWeatherService;
    private final ScoreService scoreService;
    private final RegionRepository regionRepository;
    private final LivingHealthWeatherApiCall livingHealthWeatherApiCall;
    private final ReverseGeoCoding reverseGeoCoding;
    private final CoronaService coronaService;
    private final AirPollutionService airPollutionService;
    private final AirKoreaStationUtil airKoreaStationUtil;
    private final KakaoGeoOpenApi kakaoGeoOpenApi;

    @GetMapping("/api/weather/data")
    public WeatherDataResponseDto getAllWeatherData(
            @RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude, @RequestParam("temp") int temp,
            @RequestParam("rainPer") int rainPer, @RequestParam("weather") int weather, @RequestParam("humidity") int humidity,
            @RequestParam("wind") int wind, @RequestParam("pm10") int pm10, @RequestParam("pm25") int pm25,
            @RequestParam("corona") int corona, @RequestParam("uv") int uv, @RequestParam("pollenRisk") int pollenRisk,
            @RequestParam("cold") int cold, @RequestParam("asthma") int asthma, @RequestParam("foodPoison") int foodPoison) throws ParseException, IOException {

        CoordinateDto coordinateDto = new CoordinateDto(longitude, latitude);
        ReverseGeocodingResponseDto address = reverseGeoCoding.reverseGeocoding(longitude, latitude);

        // 해당 시/구 주소를 가진 Region 객체 가져오기
        List<Region> regions = regionRepository.findByBigRegionAndSmallRegion(address.getBigRegion(), address.getSmallRegion());
        Region region = regions.get(0);

        // OPEN API 호출
        openApiService.callApi(coordinateDto, address, region);
        livingHealthWeatherApiCall.livingHealthWeatherApiCall(address, region);
        airPollutionService.getInfoByRegion(region);
        CoronaInfo coronaLocal = coronaService.getInfoByRegion(region);
        CoronaInfo coronaTotal = coronaService.getTotalInfo();

        // 클라이언트에서 보내준 사용자 선호도 수치를 담은 ScoreRequestDto 객체 생성
        ScoreRequestDto scoreRequestDto = ScoreRequestDto.hiddenBuilder()
                .tempRange(temp)
                .rainPerRange(rainPer)
                .weatherRange(weather)
                .humidityRange(humidity)
                .windRange(wind)
                .pm10Range(pm10)
                .pm25Range(pm25)
                .coronaRange(corona)
                .uvRange(uv)
                .pollenRiskRange(pollenRisk)
                .coldRange(cold)
                .asthmaRange(asthma)
                .foodPoisonRange(foodPoison)
                .build();

        // 날씨 수치들을 100점으로 반환한 점수를 담는 객체 생성
        ScoreResultResponseDto scoreResultResponseDto = new ScoreResultResponseDto();
        livingHealthWeatherService.livingHealthWthIdxConvertToScore(scoreResultResponseDto, region);
        airPollutionService.calculateScore(scoreResultResponseDto, region.getAirPollution());
        scoreResultResponseDto.setCoronaResult(coronaService.calculateScore(coronaTotal));
        openApiService.weekInfoConvertToScore(scoreResultResponseDto, region); // 주간날씨 점수 반환

        int calculatedScore = scoreService.getCalculatedScore(scoreRequestDto, scoreResultResponseDto);

        WeatherDataResponseDto responseDto = new WeatherDataResponseDto(region, coronaLocal, coronaTotal, calculatedScore);
        return responseDto;

    }

    @GetMapping("/api/corona/data")
    public CoronaInfo getCorona(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude) throws ParseException {
        ReverseGeocodingResponseDto reverseGeocodingResponseDto = reverseGeoCoding.reverseGeocoding(longitude, latitude);

        return coronaService.getInfoByRegion(reverseGeocodingResponseDto.getBigRegion());
    }

    @GetMapping("/api/air_pollution/data")
    public AirPollutionInfo getAirPollution(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude) throws ParseException {
        ReverseGeocodingResponseDto reverseGeocodingResponseDto = reverseGeoCoding.reverseGeocoding(longitude, latitude);

        Region region = regionRepository.findByBigRegionAndSmallRegion(reverseGeocodingResponseDto.getBigRegion(), reverseGeocodingResponseDto.getSmallRegion()).get(0);

        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegionName(region.getBigRegion(), region.getSmallRegion());

        AirPollutionInfo airPollution = airPollutionService.fetchAndStoreAirPollutionInfoUsingOpenApi(nearestStationName, region);

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
