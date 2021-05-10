package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.ScoreResultDto;
import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {

    public List<Integer> getCalculatedScore(ScoreWeightDto scoreWeightDto, ScoreResultDto scoreResultDto) {

        List<Integer> dayScoreList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            double temporaryValue = 0;

            temporaryValue += Double.parseDouble(scoreResultDto.getTempResult().get(i)) * scoreWeightDto.getTempWeight();
            temporaryValue += Double.parseDouble(scoreResultDto.getRainPerResult().get(i)) * scoreWeightDto.getRainPerWeight();
            temporaryValue += Double.parseDouble(scoreResultDto.getWeatherResult().get(i)) * scoreWeightDto.getWeatherWeight();
            temporaryValue += Double.parseDouble(scoreResultDto.getHumidityResult().get(i)) * scoreWeightDto.getHumidityWeight();
            temporaryValue += Double.parseDouble(scoreResultDto.getWindResult().get(i)) * scoreWeightDto.getWindWeight();

            temporaryValue += scoreResultDto.getPm10Result() * scoreWeightDto.getPm10Weight();
            temporaryValue += scoreResultDto.getPm25Result() * scoreWeightDto.getPm25Weight();
            temporaryValue += scoreResultDto.getCoronaResult() * scoreWeightDto.getCoronaWeight();
            temporaryValue += scoreResultDto.getUvResult().get(i) * scoreWeightDto.getUvWeight();
            temporaryValue += scoreResultDto.getPollenRiskResult().get(i) * scoreWeightDto.getPollenRiskWeight();
            temporaryValue += scoreResultDto.getAsthmaResult().get(i) * scoreWeightDto.getAsthmaWeight();
            temporaryValue += scoreResultDto.getFoodPoisonResult().get(i) * scoreWeightDto.getFoodPoisonWeight();

            int dayScore = (int) Math.round(temporaryValue / scoreWeightDto.getSumOfAllWeight());
            dayScoreList.add(dayScore);
        }

        return dayScoreList;
    }

}
