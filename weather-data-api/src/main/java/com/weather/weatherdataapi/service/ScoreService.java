package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.ScoreRangeResponseDto;
import com.weather.weatherdataapi.model.dto.ScoreRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ScoreService {

    public ScoreRangeResponseDto getWeatherScore100(ScoreRequestDto scoreRequestDto) {
        ArrayList<Integer> scoreRange = new ArrayList<>();
        scoreRange.add(scoreRequestDto.getCoronaRange());
        scoreRange.add(scoreRequestDto.getPm10Range());
        scoreRange.add(scoreRequestDto.getPm25Range());
        scoreRange.add(scoreRequestDto.getTempRange());
        scoreRange.add(scoreRequestDto.getRainPerRange());
        scoreRange.add(scoreRequestDto.getWeatherRange());
        scoreRange.add(scoreRequestDto.getHumidityRange());
        scoreRange.add(scoreRequestDto.getWindRange());
        scoreRange.add(scoreRequestDto.getUvRange());
        scoreRange.add(scoreRequestDto.getPollenRiskRange());
        scoreRange.add(scoreRequestDto.getColdRange());
        scoreRange.add(scoreRequestDto.getAsthmaRange());
        scoreRange.add(scoreRequestDto.getFoodPoisonRange());

        int sum = scoreRange.stream().mapToInt(Integer::intValue).sum();

        ScoreRangeResponseDto scoreRangeResponseDto = new ScoreRangeResponseDto();
        scoreRangeResponseDto.setCoronaRangeConvert(scoreRequestDto.getCoronaRange()*100/sum);
        scoreRangeResponseDto.setPm10RangeConvert(scoreRequestDto.getPm10Range()*100/sum);
        scoreRangeResponseDto.setPm25RangeConvert(scoreRequestDto.getPm25Range()*100/sum);
        scoreRangeResponseDto.setTempRangeConvert(scoreRequestDto.getTempRange()*100/sum);
        scoreRangeResponseDto.setRainPerRangeConvert(scoreRequestDto.getRainPerRange()*100/sum);
        scoreRangeResponseDto.setWeatherRangeConvert(scoreRequestDto.getWeatherRange()*100/sum);
        scoreRangeResponseDto.setHumidityRangeConvert(scoreRequestDto.getHumidityRange()*100/sum);
        scoreRangeResponseDto.setWindRangeConvert(scoreRequestDto.getWindRange()*100/sum);
        scoreRangeResponseDto.setUvRangeConvert(scoreRequestDto.getUvRange()*100/sum);
        scoreRangeResponseDto.setPollenRiskRangeConvert(scoreRequestDto.getPollenRiskRange()*100/sum);
        scoreRangeResponseDto.setColdRangeConvert(scoreRequestDto.getColdRange()*100/sum);
        scoreRangeResponseDto.setAsthmaRangeConvert(scoreRequestDto.getAsthmaRange()*100/sum);
        scoreRangeResponseDto.setFoodPoisonRangeConvert(scoreRequestDto.getFoodPoisonRange()*100/sum);

        return scoreRangeResponseDto;
    }

}
