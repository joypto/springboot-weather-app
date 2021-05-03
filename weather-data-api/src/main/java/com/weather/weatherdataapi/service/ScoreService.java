package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.requestdto.ScoreRequestDto;
import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {

    public List<Integer> getCalculatedScore(ScoreRequestDto scoreRequestDto, ScoreResultResponseDto scoreResultResponseDto) {

        List<Integer> dayScoreList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            double temporaryValue = 0;

            temporaryValue += Double.parseDouble(scoreResultResponseDto.getTempResult().get(i)) * scoreRequestDto.getTempRange();
            temporaryValue += Double.parseDouble(scoreResultResponseDto.getRainPerResult().get(i)) * scoreRequestDto.getRainPerRange();
            temporaryValue += Double.parseDouble(scoreResultResponseDto.getWeatherResult().get(i)) * scoreRequestDto.getWeatherRange();
            temporaryValue += Double.parseDouble(scoreResultResponseDto.getHumidityResult().get(i)) * scoreRequestDto.getHumidityRange();
            temporaryValue += Double.parseDouble(scoreResultResponseDto.getWindResult().get(i)) * scoreRequestDto.getWindRange();
            temporaryValue += scoreResultResponseDto.getPm10Result() * scoreRequestDto.getPm10Range();
            temporaryValue += scoreResultResponseDto.getPm25Result() * scoreRequestDto.getPm25Range();
            temporaryValue += scoreResultResponseDto.getCoronaResult() * scoreRequestDto.getCoronaRange();
            temporaryValue += scoreResultResponseDto.getUvResult().get(i) * scoreRequestDto.getUvRange();
            temporaryValue += scoreResultResponseDto.getPollenRiskResult().get(i) * scoreRequestDto.getPollenRiskRange();
            temporaryValue += scoreResultResponseDto.getAsthmaResult().get(i) * scoreRequestDto.getAsthmaRange();
            temporaryValue += scoreResultResponseDto.getFoodPoisonResult().get(i) * scoreRequestDto.getFoodPoisonRange();

            int dayScore = (int) Math.round(temporaryValue / scoreRequestDto.getSumOfAllRange());
            dayScoreList.add(dayScore);
        }

        return dayScoreList;
    }

}
