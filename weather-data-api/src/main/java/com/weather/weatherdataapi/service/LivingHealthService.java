package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.entity.Region;
import com.weather.weatherdataapi.util.HealthWthIdxConvertScore;
import com.weather.weatherdataapi.util.LivingWthIdxConvertScore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivingHealthService {

    public ScoreResultResponseDto livingHealthWthIdxConvertToScore(ScoreResultResponseDto scoreResultResponseDto, Region region) {
        // 천식폐질환지수 점수변환
        scoreResultResponseDto.setAsthmaResult(healthWthIdxConvertToScore(region.getLivingHealthWeather().getAsthmaToday()));
        // 꽃가루지수 점수변환
        scoreResultResponseDto.setPollenRiskResult(healthWthIdxConvertToScore(region.getLivingHealthWeather().getOakPollenRiskToday()));
        // 식중독지수 점수변환
        if (Integer.valueOf(region.getLivingHealthWeather().getFoodPoisonToday()) <= 55) {
            scoreResultResponseDto.setFoodPoisonResult(100);
        } else if (Integer.valueOf(region.getLivingHealthWeather().getFoodPoisonToday()) <= 70) {
            scoreResultResponseDto.setFoodPoisonResult(70);
        } else if (Integer.valueOf(region.getLivingHealthWeather().getFoodPoisonToday()) <= 85) {
            scoreResultResponseDto.setFoodPoisonResult(40);
        } else if (Integer.valueOf(region.getLivingHealthWeather().getFoodPoisonToday()) > 85) {
            scoreResultResponseDto.setFoodPoisonResult(10);
        }
        // 자외선지수 점수변환
        scoreResultResponseDto.setUvResult(livingWthIdxConvertToScore(region.getLivingHealthWeather().getUvToday()));
        return scoreResultResponseDto;
    }

    // 보건기상지수 중 식중독 지수 제외한 지수의 점수 변환
    public Integer healthWthIdxConvertToScore(String wthIdx) {
        Integer score = HealthWthIdxConvertScore.convertHealthWthIdxToScore(wthIdx);
        return score;
    }

    // 생활기상지수 중 자외선 지수의 점수 변환
    public Integer livingWthIdxConvertToScore(String wthIdx) {
        Integer score = LivingWthIdxConvertScore.convertHealthWthIdxToScore(wthIdx);
        return score;
    }

}
