package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.info.CoronaResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import com.weather.weatherdataapi.service.*;
import com.weather.weatherdataapi.util.openapi.geo.kakao.KakaoGeoApi;
import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponseDocument;
import com.weather.weatherdataapi.util.openapi.geo.naver.ReverseGeoCodingApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class DevController {

    private final RegionService regionService;

    private final ReverseGeoCodingApi reverseGeoCodingApi;
    private final KakaoGeoApi kakaoGeoApi;

    private final AirPollutionService airPollutionService;
    private final CoronaService coronaService;
    private final LivingHealthService livingHealthService;
    private final LivingHealthServiceV2 livingHealthServiceV2;

    @GetMapping("/dev/api/air_pollution")
    public AirPollutionInfo getAirPollution(RegionDto regionDto) {
        SmallRegion smallRegion = regionService.getSmallRegionByName(regionDto.getBigRegionName(), regionDto.getSmallRegionName());

        AirPollutionInfo airPollution = airPollutionService.getInfoBySmallRegion(smallRegion);

        return airPollution;
    }

    @GetMapping("/dev/api/corona")
    public CoronaResponseDto getCorona(@RequestParam("bigRegionName") String bigRegionName) {
        BigRegion bigRegion = regionService.getBigRegionByName(bigRegionName);

        CoronaInfo coronaInfo = coronaService.getInfoByBigRegion(bigRegion);
        Integer bigRegionNewCaseCount = coronaInfo.getNewLocalCaseCount() + coronaInfo.getNewForeignCaseCount();
        Integer allNewCaseCount = coronaService.getAllNewCaseCount();

        CoronaResponseDto responseDto = new CoronaResponseDto(coronaInfo.getDate(), bigRegionNewCaseCount, allNewCaseCount);
        return responseDto;
    }

    @GetMapping("/dev/api/living_health")
    public LivingHealthInfo getLivingHealth(@RequestParam("bigRegionName") String bigRegionName) {
        BigRegion bigRegion = regionService.getBigRegionByName(bigRegionName);

        LivingHealthInfo livingHealthInfo = livingHealthService.getInfoByBigRegion(bigRegion);
        return livingHealthInfo;
    }

    @GetMapping("/dev/api/v2/living_health")
    public LivingHealthInfo getLivingHealthV2(@RequestParam("bigRegionName") String bigRegionName) {
        BigRegion bigRegion = regionService.getBigRegionByName(bigRegionName);

        LivingHealthInfo livingHealthInfo = livingHealthServiceV2.getInfoByBigRegion(bigRegion);
        return livingHealthInfo;
    }

    @GetMapping("/dev/api/transcoord")
    public String getTranscoord(@RequestParam String x, @RequestParam String y) {
        try {
            KakaoGeoTranscoordResponseDocument document = kakaoGeoApi.convertWGS84ToWTM(x, y);
            return document.getX() + "," + document.getY();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/dev/api/reverse_geocoding")
    public ReverseGeocodingResponseDto getReverseGeocoding(CoordinateDto coordinateDto) {
        ReverseGeocodingResponseDto reverseGeocodingResponseDto = reverseGeoCodingApi.reverseGeocoding(coordinateDto);
        return reverseGeocodingResponseDto;
    }

    @GetMapping("/dev/api/reversegeo")
    public ReverseGeocodingResponseDto getRegionName(CoordinateDto coordinateDto) {
        try {
            return reverseGeoCodingApi.reverseGeocoding(coordinateDto);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("서비스하지 않는 지역입니다.");
            return new ReverseGeocodingResponseDto();
        }
    }
}
