package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.ScoreResultDto;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    /**
     * /api/total/data 요청에 알맞는 ResponseEntity 객체를 생성하여 반환하는 메소드입니다.
     *
     * @param identification 이번 요청을 실시한 사용자의 identification 입니다.
     * @param coordinateDto  요청을 원하는 지역의 좌표입니다.
     * @return 응답 객체입니다. RestController에서 이 응답값을 바로 리턴하면 됩니다.
     */
    public ResponseEntity<TotalDataResponseDto> getTotalDataResponse(String identification, CoordinateDto coordinateDto) throws IOException {
        RegionDto regionDto = regionService.getRegionName(coordinateDto);

        return getTotalDataResponse(identification, regionDto);
    }

    /**
     * /api/total/data 요청에 알맞는 ResponseEntity 객체를 생성하여 반환하는 메소드입니다.
     *
     * @param identification 이번 요청을 실시한 사용자의 identification 입니다.
     * @param regionDto      요청을 원하는 지역입니다.
     * @return 응답 객체입니다. RestController에서 이 응답값을 바로 리턴하면 됩니다.
     */
    public ResponseEntity<TotalDataResponseDto> getTotalDataResponse(String identification, RegionDto regionDto) throws IOException {

        User user = userService.getOrCreateGuarantedNonCachedUserByIdentification(identification);

        userService.updateCurrentRegion(user, regionDto.getBigRegionName() + " " + regionDto.getSmallRegionName());

        ScoreWeightDto scoreWeightDto = new ScoreWeightDto(user);

        TotalDataResponseDto responseDto = getTotalData(regionDto, scoreWeightDto);
        HttpHeaders responseHeaders = userService.createHeadersWithUserIdentification(user);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(responseDto);
    }

    public TotalDataResponseDto getTotalData(RegionDto regionDto, ScoreWeightDto scoreWeightDto) throws IOException {

        // 해당 시/구 주소를 가진 Region 객체 가져오기
        BigRegion currentBigRegion = regionService.getBigRegionByName(regionDto.getBigRegionName());
        SmallRegion currentSmallRegion = regionService.getSmallRegionByName(regionDto.getBigRegionName(), regionDto.getSmallRegionName());

        // 객체 생성
        ScoreResultDto scoreResultDto = new ScoreResultDto();
        TotalDataResponseDto weatherDataResponseDto = new TotalDataResponseDto(currentBigRegion, currentSmallRegion);

        // 여기서부터 setInfoAndScore 로직 시작
        coronaService.setInfoAndScore(currentBigRegion, scoreResultDto, weatherDataResponseDto);
        livingHealthService.setInfoAndScore(currentBigRegion, scoreResultDto, weatherDataResponseDto);
        weatherService.setInfoAndScore(currentSmallRegion, scoreResultDto, weatherDataResponseDto);
        airPollutionService.setInfoAndScore(currentSmallRegion, scoreResultDto, weatherDataResponseDto);

        List<Integer> dayScoreList = scoreService.getCalculatedScore(scoreWeightDto, scoreResultDto);
        weatherDataResponseDto.setDayScoreList(dayScoreList);

        return weatherDataResponseDto;

    }


}
