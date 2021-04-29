package com.weather.originapi.service;

import com.weather.originapi.model.dto.ScoreRangeResponseDto;
import com.weather.originapi.model.dto.ScoreRequestDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ScoreService {

    public void getWeatherScoreRange(ScoreRequestDto scoreRequestDto) {
        ArrayList<Integer> scoreRange = new ArrayList<>();
        scoreRange.add(scoreRequestDto.getCoronaRange());
        scoreRange.add(scoreRequestDto.getFineDustRange());
        scoreRange.add(scoreRequestDto.getTempRange());
        scoreRange.add(scoreRequestDto.getRainPerRange());
        scoreRange.add(scoreRequestDto.getWeatherRange());
        scoreRange.add(scoreRequestDto.getHumidityRange());
        scoreRange.add(scoreRequestDto.getWindRange());
        scoreRange.add(scoreRequestDto.getUvRange());
        scoreRange.add(scoreRequestDto.getPollenRiskRange());
        scoreRange.add(scoreRequestDto.getColdRange());

        int sum = scoreRange.stream().mapToInt(Integer::intValue).sum();

        ScoreRangeResponseDto scoreRangeResponseDto = new ScoreRangeResponseDto();
        scoreRangeResponseDto.setCoronaRangeConvert(scoreRequestDto.getCoronaRange()*100/sum);
        scoreRangeResponseDto.setFineDustRangeConvert(scoreRequestDto.getFineDustRange()*100/sum);
    }

}
