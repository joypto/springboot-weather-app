package com.weather.originapi.controller;


import com.weather.originapi.model.dto.ScoreRangeResponseDto;
import com.weather.originapi.model.dto.ScoreRequestDto;
import com.weather.originapi.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping("/api/weather/scores")
    public ScoreRangeResponseDto getWeatherScore(@RequestBody ScoreRequestDto scoreRequestDto) {
        ScoreRangeResponseDto scoreRangeResponseDto = scoreService.getWeatherScoreRange(scoreRequestDto);
        return scoreRangeResponseDto;
    }
}
