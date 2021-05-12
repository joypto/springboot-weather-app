package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.ScoreResultDto;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.User;
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

    public TotalDataResponseDto getTotalData(User user, RegionDto totalDataRequestDto) throws IOException {

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

        // user 정보에 현재 요청한 위치를 갱신합니다.
        userService.updateCurrentRegion(user, totalDataRequestDto.getBigRegionName() + " " + totalDataRequestDto.getSmallRegionName());

        // 클라이언트에서 보내준 사용자 선호도 수치를 담은 ScoreRequestDto 객체 생성
        ScoreWeightDto scoreWeightDto = new ScoreWeightDto(user);

        List<Integer> dayScoreList = scoreService.getCalculatedScore(scoreWeightDto, scoreResultDto);
        weatherDataResponseDto.setDayScoreList(dayScoreList);

        return weatherDataResponseDto;

    }


}
