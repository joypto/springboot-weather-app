package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.requestdto.ScoreRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.WeatherDataResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.UserPreference;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.repository.SmallRegionRepository;
import com.weather.weatherdataapi.repository.UserPreferenceRepository;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaStationUtil;
import com.weather.weatherdataapi.util.openapi.geo.kakao.KakaoGeoApi;
import com.weather.weatherdataapi.util.openapi.geo.naver.ReverseGeoCodingApi;
import com.weather.weatherdataapi.util.openapi.living_health.LivingHealthApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

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

    public WeatherDataResponseDto getTotalData(CoordinateDto coordinateDto, String token) throws ParseException {

        // 해당 위경도로 시/구 주소 문자열 반환
        ReverseGeocodingResponseDto address = reverseGeoCodingApi.reverseGeocoding(coordinateDto);

        // 해당 시/구 주소를 가진 Region 객체 가져오기
        BigRegion currentBigRegion = bigRegionRepository.findByBigRegionName(address.getBigRegion());
        SmallRegion currentSmallRegion = smallRegionRepository.findByBigRegionAndSmallRegionName(currentBigRegion, address.getSmallRegion());

        // 객체 생성
        ScoreResultResponseDto scoreResultResponseDto = new ScoreResultResponseDto();
        WeatherDataResponseDto weatherDataResponseDto = new WeatherDataResponseDto();

        // 여기서부터 setInfoAndScore 로직 시작
        coronaService.setInfoAndScore(currentBigRegion);
        livingHealthService.setInfoAndScore(currentBigRegion);
        weatherService.setInfoAndScore(currentSmallRegion);
        airPollutionService.setInfoAndScore(currentSmallRegion);

        // 식별값으로 DB 에서 유저 선호도 불러오기
        UserPreference currentUserPreference = userPreferenceService.getCurrentUserPreference(token);

        // 클라이언트에서 보내준 사용자 선호도 수치를 담은 ScoreRequestDto 객체 생성
        ScoreRequestDto scoreRequestDto = new ScoreRequestDto(currentUserPreference);

        // 날씨 수치들을 100점으로 반환한 점수를 담는 객체 생성
        livingHealthService.livingHealthWthIdxConvertToScore(scoreResultResponseDto, currentBigRegion);
        airPollutionService.calculateScore(scoreResultResponseDto, currentSmallRegion.getAirPollutionInfoList().get(0));
        scoreResultResponseDto.setCoronaResult(coronaService.calculateScore(coronaTotalNewCaseCount));
        weatherService.weekInfoConvertToScore(scoreResultResponseDto, currentSmallRegion); // 주간날씨 점수 반환

        List<Integer> dayScoreList = scoreService.getCalculatedScore(scoreRequestDto, scoreResultResponseDto);
        WeatherDataResponseDto responseDto = new WeatherDataResponseDto(currentBigRegion, currentSmallRegion, coronaLocal, coronaTotalNewCaseCount, dayScoreList);
        return responseDto;

    }
}