package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.ScoreResultDto;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.util.openapi.geo.naver.ReverseGeoCodingApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TotalDataService {

    private final AirPollutionService airPollutionService;
    private final CoronaService coronaService;
    private final LivingHealthService livingHealthService;
    private final WeatherService weatherService;
    private final UserService userService;
    private final ScoreService scoreService;
    private final RegionService regionService;
    private final ReverseGeoCodingApi reverseGeoCodingApi;

    public TotalDataResponseDto getTotalData(RegionDto totalDataRequestDto, String identification) throws IOException {

        // 해당 시/구 주소를 가진 Region 객체 가져오기
        BigRegion currentBigRegion = regionService.getBigRegionByName(totalDataRequestDto.getBigRegionName());
        SmallRegion currentSmallRegion = regionService.getSmallRegionByName(totalDataRequestDto.getBigRegionName(), totalDataRequestDto.getSmallRegionName());

        // 객체 생성
        ScoreResultDto scoreResultDto = new ScoreResultDto();
        TotalDataResponseDto weatherDataResponseDto = new TotalDataResponseDto(currentBigRegion, currentSmallRegion);

        // 여기서부터 setInfoAndScore 로직 시작
        coronaService.setInfoAndScore(currentBigRegion, scoreResultDto, weatherDataResponseDto);
        livingHealthService.setInfoAndScore(currentBigRegion, scoreResultDto, weatherDataResponseDto);
        weatherService.setInfoAndScore(currentSmallRegion, scoreResultDto, weatherDataResponseDto);
        airPollutionService.setInfoAndScore(currentSmallRegion, scoreResultDto, weatherDataResponseDto);

        // 식별값으로 DB 에서 유저 선호도 불러오기
        User user = userService.getOrCreateUserByIdentification(identification);
        userService.updateCurrentRegion(user, totalDataRequestDto.getBigRegionName() + " " + totalDataRequestDto.getSmallRegionName());

        // 클라이언트에서 보내준 사용자 선호도 수치를 담은 ScoreRequestDto 객체 생성
        ScoreWeightDto scoreWeightDto = new ScoreWeightDto(user);

        List<Integer> dayScoreList = scoreService.getCalculatedScore(scoreWeightDto, scoreResultDto);
        weatherDataResponseDto.setDayScoreList(dayScoreList);

        weatherDataResponseDto.setIdentification(user.getIdentification());
        return weatherDataResponseDto;

    }

    public RegionDto getRegionName(CoordinateDto coordinateDto) {
        // 해당 위경도로 시/구 주소 문자열 반환
        ReverseGeocodingResponseDto address = reverseGeoCodingApi.reverseGeocoding(coordinateDto);
        return new RegionDto(address.getBigRegion(), address.getSmallRegion());
    }
}
