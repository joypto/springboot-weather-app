package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.requestdto.ScoreRequestDto;
import com.weather.weatherdataapi.model.dto.requestdto.TotalDataRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.WeatherDataResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.UserPreference;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.repository.SmallRegionRepository;
import com.weather.weatherdataapi.util.openapi.geo.naver.ReverseGeoCodingApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
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
    private final UserPreferenceService userPreferenceService;
    private final ScoreService scoreService;
    private final BigRegionRepository bigRegionRepository;
    private final SmallRegionRepository smallRegionRepository;
    private final ReverseGeoCodingApi reverseGeoCodingApi;

    public WeatherDataResponseDto getTotalData(TotalDataRequestDto totalDataRequestDto, String token) throws IOException {

        // 해당 시/구 주소를 가진 Region 객체 가져오기
        BigRegion currentBigRegion = bigRegionRepository.findByBigRegionName(totalDataRequestDto.getCurrentBigRegionName());
        SmallRegion currentSmallRegion = smallRegionRepository.findByBigRegionAndSmallRegionName(currentBigRegion, totalDataRequestDto.getCurrentSmallRegionName());

        // 객체 생성
        ScoreResultResponseDto scoreResultResponseDto = new ScoreResultResponseDto();
        WeatherDataResponseDto weatherDataResponseDto = new WeatherDataResponseDto(currentBigRegion, currentSmallRegion);

        // 여기서부터 setInfoAndScore 로직 시작
        coronaService.setInfoAndScore(currentBigRegion, scoreResultResponseDto, weatherDataResponseDto);
        livingHealthService.setInfoAndScore(currentBigRegion, scoreResultResponseDto, weatherDataResponseDto);
        weatherService.setInfoAndScore(currentSmallRegion, scoreResultResponseDto, weatherDataResponseDto);
        airPollutionService.setInfoAndScore(currentSmallRegion, scoreResultResponseDto, weatherDataResponseDto);

        // 식별값으로 DB 에서 유저 선호도 불러오기
        UserPreference currentUserPreference = userPreferenceService.getCurrentUserPreference(token);
        userPreferenceService.setUserCurrentRegion(currentUserPreference, totalDataRequestDto.getCurrentBigRegionName() + " " + totalDataRequestDto.getCurrentSmallRegionName());

        // 클라이언트에서 보내준 사용자 선호도 수치를 담은 ScoreRequestDto 객체 생성
        ScoreRequestDto scoreRequestDto = new ScoreRequestDto(currentUserPreference);

        List<Integer> dayScoreList = scoreService.getCalculatedScore(scoreRequestDto, scoreResultResponseDto);
        weatherDataResponseDto.setDayScoreList(dayScoreList);
        return weatherDataResponseDto;

    }

    public TotalDataRequestDto getRegionName(CoordinateDto coordinateDto) throws ParseException {
        // 해당 위경도로 시/구 주소 문자열 반환
        ReverseGeocodingResponseDto address = reverseGeoCodingApi.reverseGeocoding(coordinateDto);
        return new TotalDataRequestDto(address.getBigRegion(), address.getSmallRegion());
    }
}
