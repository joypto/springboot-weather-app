package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.ScoreRequestDto;
import com.weather.weatherdataapi.model.dto.ScoreResultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreService {

    public int getCalculatedScore(ScoreRequestDto scoreRequestDto, ScoreResultResponseDto scoreResultResponseDto) {
        double temporaryValue = 0;

//        temporaryValue += Double.parseDouble(scoreResultResponseDto.getTempResult().get(0)) * scoreRequestDto.getTempRange();
        temporaryValue += Double.parseDouble(scoreResultResponseDto.getRainPerResult().get(0)) * scoreRequestDto.getRainPerRange();
        temporaryValue += Double.parseDouble(scoreResultResponseDto.getWeatherResult().get(0)) * scoreRequestDto.getWeatherRange();
        temporaryValue += Double.parseDouble(scoreResultResponseDto.getHumidityResult().get(0)) * scoreRequestDto.getHumidityRange();
        temporaryValue += Double.parseDouble(scoreResultResponseDto.getWindResult().get(0)) * scoreRequestDto.getWindRange();
        temporaryValue += scoreResultResponseDto.getPm10Result() * scoreRequestDto.getPm10Range();
        temporaryValue += scoreResultResponseDto.getPm25Result() * scoreRequestDto.getPm25Range();
        temporaryValue += scoreResultResponseDto.getCoronaResult() * scoreRequestDto.getCoronaRange();
        temporaryValue += scoreResultResponseDto.getUvResult() * scoreRequestDto.getUvRange();
        temporaryValue += scoreResultResponseDto.getPollenRiskResult() * scoreRequestDto.getPollenRiskRange();
        temporaryValue += scoreResultResponseDto.getColdResult() * scoreRequestDto.getColdRange();
        temporaryValue += scoreResultResponseDto.getAsthmaResult() * scoreRequestDto.getAsthmaRange();
        temporaryValue += scoreResultResponseDto.getFoodPoisonResult() * scoreRequestDto.getFoodPoisonRange();

        int totalScore = (int) Math.round(temporaryValue / scoreRequestDto.getSumOfAllRange());

        return totalScore;
    }

}
