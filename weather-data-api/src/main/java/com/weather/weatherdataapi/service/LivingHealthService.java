package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import com.weather.weatherdataapi.repository.info.LivingHealthInfoRepository;
import com.weather.weatherdataapi.util.HealthScoreUtil;
import com.weather.weatherdataapi.util.LivingScoreUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivingHealthService {

    private final LivingHealthInfoRepository livingHealthInfoRepository;

    public ScoreResultResponseDto livingHealthWthIdxConvertToScore(ScoreResultResponseDto scoreResultResponseDto, SmallRegion smallRegion) {

        LivingHealthInfo livingHealthInfo = livingHealthInfoRepository.findFirstBySmallRegionOrderByCreatedAt(smallRegion);

        // 천식폐질환지수 점수변환
        scoreResultResponseDto.setAsthmaResult(healthWthIdxConvertToScore(livingHealthInfo.getAsthmaToday()));
        // 꽃가루지수 점수변환
        scoreResultResponseDto.setPollenRiskResult(healthWthIdxConvertToScore(livingHealthInfo.getOakPollenRiskToday()));
        // 식중독지수 점수변환
        if (Integer.parseInt(livingHealthInfo.getFoodPoisonToday()) <= 55) {
            scoreResultResponseDto.setFoodPoisonResult(100);
        } else if (Integer.parseInt(livingHealthInfo.getFoodPoisonToday()) <= 70) {
            scoreResultResponseDto.setFoodPoisonResult(70);
        } else if (Integer.parseInt(livingHealthInfo.getFoodPoisonToday()) <= 85) {
            scoreResultResponseDto.setFoodPoisonResult(40);
        } else if (Integer.parseInt(livingHealthInfo.getFoodPoisonToday()) > 85) {
            scoreResultResponseDto.setFoodPoisonResult(10);
        }
        // 자외선지수 점수변환
        scoreResultResponseDto.setUvResult(livingWthIdxConvertToScore(livingHealthInfo.getUvToday()));
        return scoreResultResponseDto;
    }

    // 보건기상지수 중 식중독 지수 제외한 지수의 점수 변환
    public Integer healthWthIdxConvertToScore(String wthIdx) {
        Integer score = HealthScoreUtil.convertHealthWthIdxToScore(wthIdx);
        return score;
    }

    // 생활기상지수 중 자외선 지수의 점수 변환
    public Integer livingWthIdxConvertToScore(String wthIdx) {
        Integer score = LivingScoreUtil.convertHealthWthIdxToScore(wthIdx);
        return score;
    }

}
