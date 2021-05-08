package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.ScoreWeightDto;
import com.weather.weatherdataapi.model.dto.ScoreResultDto;
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

            temporaryValue += Double.parseDouble(scoreResultDto.getTempResult().get(i)) * scoreWeightDto.getTempRange();
            temporaryValue += Double.parseDouble(scoreResultDto.getRainPerResult().get(i)) * scoreWeightDto.getRainPerRange();
            temporaryValue += Double.parseDouble(scoreResultDto.getWeatherResult().get(i)) * scoreWeightDto.getWeatherRange();
            temporaryValue += Double.parseDouble(scoreResultDto.getHumidityResult().get(i)) * scoreWeightDto.getHumidityRange();
            temporaryValue += Double.parseDouble(scoreResultDto.getWindResult().get(i)) * scoreWeightDto.getWindRange();
            temporaryValue += scoreResultDto.getPm10Result() * scoreWeightDto.getPm10Range();
            temporaryValue += scoreResultDto.getPm25Result() * scoreWeightDto.getPm25Range();
            temporaryValue += scoreResultDto.getCoronaResult() * scoreWeightDto.getCoronaRange();
            temporaryValue += scoreResultDto.getUvResult().get(i) * scoreWeightDto.getUvRange();
            temporaryValue += scoreResultDto.getPollenRiskResult().get(i) * scoreWeightDto.getPollenRiskRange();
            temporaryValue += scoreResultDto.getAsthmaResult().get(i) * scoreWeightDto.getAsthmaRange();
            temporaryValue += scoreResultDto.getFoodPoisonResult().get(i) * scoreWeightDto.getFoodPoisonRange();

            int dayScore = (int) Math.round(temporaryValue / scoreWeightDto.getSumOfAllRange());
            dayScoreList.add(dayScore);
        }

        return dayScoreList;
    }

}
