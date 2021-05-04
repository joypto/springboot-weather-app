package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.WeatherDataResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.repository.SmallRegionRepository;
import com.weather.weatherdataapi.service.AirPollutionService;
import com.weather.weatherdataapi.service.CoronaService;
import com.weather.weatherdataapi.service.TotalDataService;
import com.weather.weatherdataapi.util.openapi.geo.kakao.KakaoGeoApi;
import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponseDocument;
import com.weather.weatherdataapi.util.openapi.geo.naver.ReverseGeoCodingApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class WeatherDataController {

    private final ReverseGeoCodingApi reverseGeoCoding;
    private final CoronaService coronaService;
    private final AirPollutionService airPollutionService;
    private final KakaoGeoApi kakaoGeoOpenApi;
    private final BigRegionRepository bigRegionRepository;
    private final SmallRegionRepository smallRegionRepository;
    private final TotalDataService totalDataService;

    @GetMapping("/api/weather/data")
    public WeatherDataResponseDto getAllWeatherData(
            @RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude, @RequestHeader("token") String token) throws ParseException, IOException {

        CoordinateDto coordinateDto = new CoordinateDto(longitude, latitude);
        return totalDataService.getTotalData(coordinateDto, token);
    }

    @GetMapping("/api/air_pollution/data")
    public AirPollutionInfo getAirPollution(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude) throws ParseException {
        CoordinateDto coordinateDto = new CoordinateDto(longitude, latitude);
        ReverseGeocodingResponseDto reverseGeocodingResponseDto = reverseGeoCoding.reverseGeocoding(coordinateDto);

        BigRegion bigRegion = bigRegionRepository.findByBigRegionName(reverseGeocodingResponseDto.getBigRegion());
        SmallRegion smallRegion = smallRegionRepository.findByBigRegionAndSmallRegionName(bigRegion, reverseGeocodingResponseDto.getSmallRegion());

        AirPollutionInfo airPollution = airPollutionService.fetchAndStoreAirPollutionInfoUsingOpenApi(smallRegion);

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
