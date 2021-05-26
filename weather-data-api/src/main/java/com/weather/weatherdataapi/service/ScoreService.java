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

        int sumOfAllWeight = 0;
        if (scoreResultDto.isWeatherValid())
            sumOfAllWeight += scoreWeightDto.getSumOfWeatherWeight();

        if (scoreResultDto.isLivingHealthValid())
            sumOfAllWeight += scoreWeightDto.getSumOfLivingHealthWeight();

        if (scoreResultDto.isAirPollutionValid())
            sumOfAllWeight += scoreWeightDto.getSumOfAirPollutionWeight();

        if (scoreResultDto.isCoronaValid())
            sumOfAllWeight += scoreWeightDto.getCoronaWeight();

        for (int i = 0; i < 7; i++) {
            double temporaryValue = 0;

            if (scoreResultDto.isWeatherValid()) {
                temporaryValue += Double.parseDouble(scoreResultDto.getTempResult().get(i)) * scoreWeightDto.getTempWeight();
                temporaryValue += Double.parseDouble(scoreResultDto.getRainPerResult().get(i)) * scoreWeightDto.getRainPerWeight();
                temporaryValue += Double.parseDouble(scoreResultDto.getWeatherResult().get(i)) * scoreWeightDto.getWeatherWeight();
                temporaryValue += Double.parseDouble(scoreResultDto.getHumidityResult().get(i)) * scoreWeightDto.getHumidityWeight();
                temporaryValue += Double.parseDouble(scoreResultDto.getWindResult().get(i)) * scoreWeightDto.getWindWeight();
            }

            if (scoreResultDto.isLivingHealthValid()) {
                temporaryValue += scoreResultDto.getUvResult().get(i) * scoreWeightDto.getUvWeight();
                temporaryValue += scoreResultDto.getPollenRiskResult().get(i) * scoreWeightDto.getPollenRiskWeight();
                temporaryValue += scoreResultDto.getAsthmaResult().get(i) * scoreWeightDto.getAsthmaWeight();
                temporaryValue += scoreResultDto.getFoodPoisonResult().get(i) * scoreWeightDto.getFoodPoisonWeight();
            }

            // 미세먼지와 코로나 정보는 예측 데이터가 없습니다.
            // 따라서 일주일간의 점수에 반영되는 미세먼지 수치와 코로나 수치는 지금의 데이터가 유지된다고 가정하고 계산합니다.
            if (scoreResultDto.isAirPollutionValid()) {
                temporaryValue += scoreResultDto.getPm10Result() * scoreWeightDto.getPm10Weight();
                temporaryValue += scoreResultDto.getPm25Result() * scoreWeightDto.getPm25Weight();
            }

            if (scoreResultDto.isCoronaValid()) {
                temporaryValue += scoreResultDto.getCoronaResult() * scoreWeightDto.getCoronaWeight();
            }

            int dayScore = (int) Math.round(temporaryValue / sumOfAllWeight);
            dayScoreList.add(dayScore);
        }

        return dayScoreList;
    }

}
