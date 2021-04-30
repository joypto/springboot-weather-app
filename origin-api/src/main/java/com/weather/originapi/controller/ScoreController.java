package com.weather.originapi.controller;


import com.weather.originapi.model.dto.RegionResponseDto;
import com.weather.originapi.model.dto.ScoreRangeResponseDto;
import com.weather.originapi.model.dto.ScoreRequestDto;
import com.weather.originapi.model.dto.ScoreResultResponseDto;
import com.weather.originapi.service.ScoreService;
import com.weather.originapi.util.WeatherData;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;
    private final WeatherData weatherData;

    @PostMapping("/api/weather/scores")
    public ScoreResultResponseDto getWeatherScore(@RequestBody ScoreRequestDto scoreRequestDto) throws IOException, ParseException {
        ScoreRangeResponseDto scoreRangeResponseDto = scoreService.getWeatherScoreRange(scoreRequestDto);
        RegionResponseDto regionResponseDto = weatherData.weatherDataForScore();
        ScoreResultResponseDto scoreResultResponseDto = scoreService.calculateScore(regionResponseDto);

        return scoreResultResponseDto;
    }

    @GetMapping("api/test")
    public RegionResponseDto test() throws IOException, ParseException {
        RegionResponseDto regionResponseDto = weatherData.weatherDataForScore();
        System.out.println(regionResponseDto.getRegion());
        return regionResponseDto;
    }
}
