package com.weather.originapi.service;

import com.weather.originapi.model.dto.RegionResponseDto;
import com.weather.originapi.model.dto.ScoreRangeResponseDto;
import com.weather.originapi.model.dto.ScoreRequestDto;
import com.weather.originapi.model.dto.ScoreResultResponseDto;
import com.weather.originapi.util.WeatherIdxConvertScore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ScoreService {

    public ScoreRangeResponseDto getWeatherScoreRange(ScoreRequestDto scoreRequestDto) {
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
        scoreRangeResponseDto.setTempRangeConvert(scoreRequestDto.getTempRange()*100/sum);
        scoreRangeResponseDto.setRainPerRangeConvert(scoreRequestDto.getRainPerRange()*100/sum);
        scoreRangeResponseDto.setWeatherRangeConvert(scoreRequestDto.getWeatherRange()*100/sum);
        scoreRangeResponseDto.setHumidityRangeConvert(scoreRequestDto.getHumidityRange()*100/sum);
        scoreRangeResponseDto.setWindRangeConvert(scoreRequestDto.getWindRange()*100/sum);
        scoreRangeResponseDto.setUvRangeConvert(scoreRequestDto.getUvRange()*100/sum);
        scoreRangeResponseDto.setPollenRiskRangeConvert(scoreRequestDto.getPollenRiskRange()*100/sum);
        scoreRangeResponseDto.setColdRangeConvert(scoreRequestDto.getColdRange()*100/sum);

        return scoreRangeResponseDto;
    }

    public ScoreResultResponseDto calculateScore(RegionResponseDto regionResponseDto) {
        System.out.println(regionResponseDto.getUvToday());

        Integer uvTodayScore = WeatherIdxConvertScore.convertHealthWthIdxToScore("1");
        ScoreResultResponseDto scoreResultResponseDto = new ScoreResultResponseDto();
        scoreResultResponseDto.setUvResult(uvTodayScore);

        return scoreResultResponseDto;
    }
}
