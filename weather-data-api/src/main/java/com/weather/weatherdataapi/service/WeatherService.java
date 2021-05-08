package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.WeatherDayInfo;
import com.weather.weatherdataapi.model.entity.info.WeatherWeekInfo;
import com.weather.weatherdataapi.model.vo.redis.WeatherDayRedisVO;
import com.weather.weatherdataapi.model.vo.redis.WeatherWeekRedisVO;
import com.weather.weatherdataapi.repository.redis.WeatherDayRedisRepository;
import com.weather.weatherdataapi.repository.redis.WeatherWeekRedisRepository;
import com.weather.weatherdataapi.util.openapi.weather.WeatherApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherService {
    private final WeatherApi weatherGatherApi;
    private final WeatherWeekRedisRepository weatherWeekRedisRepository;
    private final WeatherDayRedisRepository weatherDayRedisRepository;


    // 하루 지난 캐쉬 삭제
    @Transactional
    public void fetchAndStoreWeatherInfoUsingOpenApi() {
        weatherDayRedisRepository.deleteAll();
        weatherWeekRedisRepository.deleteAll();
        log.info("fetchAndStoreWeatherInfo:: 날씨정보 캐시를 비웠습니다.");

    }

    // 날씨 정보 셋팅
    public void setInfoAndScore(SmallRegion wantRegion, ScoreResultResponseDto scoreResultResponseDto, TotalDataResponseDto weatherDataResponseDto) throws IOException {
        try {
            WeatherWeekRedisVO weekInfoRedis = weatherWeekRedisRepository.findById(wantRegion.getAdmCode()).orElseThrow(() -> new NullPointerException("?"));
            WeatherDayRedisVO dayInfoRedis = weatherDayRedisRepository.findById(wantRegion.getAdmCode()).orElseThrow(() -> new NullPointerException("?"));
            WeatherWeekInfo weekInfo = new WeatherWeekInfo(weekInfoRedis);
            WeatherDayInfo dayInfo = new WeatherDayInfo(dayInfoRedis);
            weatherDataResponseDto.setWeekInfo(weekInfo);
            weatherDataResponseDto.setDayInfo(dayInfo);
            convertInfoToScore(scoreResultResponseDto, weekInfo);
            log.info("캐시로 날씨 데이터 불러오기");
        } catch (Exception e) {
            log.info("캐시로 날씨 데이터 불러오기 실패");
            WeatherWeekInfo weekInfo = weatherGatherApi.callWeather(wantRegion, weatherDataResponseDto);
            convertInfoToScore(scoreResultResponseDto, weekInfo);
        }
    }


    public void convertInfoToScore(ScoreResultResponseDto scoreResultResponseDto, WeatherWeekInfo weekInfo) throws IOException {
        try {
            // 날짜별 환산점수 변환 시작
            List<String> getRainPer = new ArrayList<>();
            List<String> getWeather = new ArrayList<>();
            List<String> getWind = new ArrayList<>();
            List<String> getHumidity = new ArrayList<>();
            List<String> getTemp = new ArrayList<>();
            LocalDate time = LocalDate.now();
            String getMonth = time.toString().substring(5, 7);
            for (int i = 0; i < 7; i++) {
                getRainScore(weekInfo, getRainPer, i);
                getWindScore(weekInfo, getWind, i);
                getHumidityScore(weekInfo, getHumidity, i);
                getWeatherScore(weekInfo, getWeather, i);
                getWeatherScore(weekInfo, getTemp, i, getMonth);
            }

            scoreResultResponseDto.setRainPerResult(getRainPer);
            scoreResultResponseDto.setWeatherResult(getWeather);
            scoreResultResponseDto.setHumidityResult(getHumidity);
            scoreResultResponseDto.setWindResult(getWind);
            scoreResultResponseDto.setTempResult(getTemp);
        } catch (Exception e) {
            List<String> getRainPer = new ArrayList<>(Arrays.asList("100", "100", "100", "100", "100", "100", "100"));
            List<String> getWeather = new ArrayList<>(Arrays.asList("100", "100", "100", "100", "100", "100", "100"));
            List<String> getWind = new ArrayList<>(Arrays.asList("100", "100", "100", "100", "100", "100", "100"));
            List<String> getHumidity = new ArrayList<>(Arrays.asList("100", "100", "100", "100", "100", "100", "100"));
            List<String> getTemp = new ArrayList<>(Arrays.asList("100", "100", "100", "100", "100", "100", "100"));
            scoreResultResponseDto.setRainPerResult(getRainPer);
            scoreResultResponseDto.setWeatherResult(getWeather);
            scoreResultResponseDto.setHumidityResult(getHumidity);
            scoreResultResponseDto.setWindResult(getWind);
            scoreResultResponseDto.setTempResult(getTemp);
            log.info("날씨 점수 에러 발생 비상 비상 비상 비상 비상 비상 비상 비상");
        }
    }

    // 날짜별 강수확률 변환점수
    public void getRainScore(WeatherWeekInfo weekInfo, List<String> getRainPer, int i) {
        if (Double.parseDouble(weekInfo.getRainPer().get(i)) <= 0.2d) {
            getRainPer.add("100");
        } else if (Double.parseDouble(weekInfo.getRainPer().get(i)) <= 0.5d) {
            getRainPer.add("70");
        } else if (Double.parseDouble(weekInfo.getRainPer().get(i)) <= 0.7d) {
            getRainPer.add("40");
        } else {
            getRainPer.add("10");
        }
    }

    //바람 점수를 반환
    public void getWindScore(WeatherWeekInfo weekInfo, List<String> getWind, int i) {
        if (Double.parseDouble(weekInfo.getWindSpeed().get(i)) <= 3.3d) {
            getWind.add("100");
        } else if (Double.parseDouble(weekInfo.getWindSpeed().get(i)) <= 5.4d) {
            getWind.add("70");
        } else if (Double.parseDouble(weekInfo.getWindSpeed().get(i)) <= 10.7d) {
            getWind.add("40");
        } else {
            getWind.add("10");
        }
    }

    //습도 점수를 반환
    public void getHumidityScore(WeatherWeekInfo weekInfo, List<String> getHumidity, int i) {
        if (40d <= Double.parseDouble(weekInfo.getHumidity().get(i)) && Double.parseDouble(weekInfo.getHumidity().get(i)) <= 60d) {
            getHumidity.add("100");
        } else if (30d <= Double.parseDouble(weekInfo.getHumidity().get(i)) && Double.parseDouble(weekInfo.getHumidity().get(i)) <= 70d) {
            getHumidity.add("70");
        } else if (20d <= Double.parseDouble(weekInfo.getHumidity().get(i)) && Double.parseDouble(weekInfo.getHumidity().get(i)) <= 80d) {
            getHumidity.add("40");
        } else {
            getHumidity.add("10");
        }
    }

    //날씨 점수 반환
    public void getWeatherScore(WeatherWeekInfo weekInfo, List<String> getWeather, int i) {
        switch (weekInfo.getWeatherIcon().get(i)) {
            case "01d":
            case "01n":
            case "02d":
            case "02n":
                getWeather.add("100");
                break;
            case "03d":
            case "03n":
            case "04d":
            case "04n":
                getWeather.add("70");
                break;
            case "09d":
            case "09n":
            case "10d":
            case "10n":
            case "13d":
            case "13n":
                getWeather.add("40");
                break;
            case "11d":
            case "11n":
            case "50d":
            case "50n":
                getWeather.add("10");
                break;

        }
    }

    public void getWeatherScore(WeatherWeekInfo weekInfo, List<String> getTemp, int i, String getMonth) {
        switch (getMonth) {
            // 봄
            case "03":
            case "04":
            case "05":
                if (18d <= Double.parseDouble(weekInfo.getTmp().get(i)) && Double.parseDouble(weekInfo.getTmp().get(i)) <= 22d) {
                    getTemp.add("100");
                } else if (15d <= Double.parseDouble(weekInfo.getTmp().get(i)) && Double.parseDouble(weekInfo.getTmp().get(i)) <= 25d) {
                    getTemp.add("70");
                } else if (12d <= Double.parseDouble(weekInfo.getTmp().get(i)) && Double.parseDouble(weekInfo.getTmp().get(i)) <= 28d) {
                    getTemp.add("40");
                } else {
                    getTemp.add("10");
                }
                break;
            //여름
            case "06":
            case "07":
            case "08":
                if (Double.parseDouble(weekInfo.getTmp().get(i)) <= 24d) {
                    getTemp.add("100");
                } else if (Double.parseDouble(weekInfo.getTmp().get(i)) <= 27d) {
                    getTemp.add("70");
                } else if (Double.parseDouble(weekInfo.getTmp().get(i)) <= 30d) {
                    getTemp.add("40");
                } else {
                    getTemp.add("10");
                }
                break;
            //가을
            case "09":
            case "10":
            case "11":
                if (14d <= Double.parseDouble(weekInfo.getTmp().get(i)) && Double.parseDouble(weekInfo.getTmp().get(i)) <= 18d) {
                    getTemp.add("100");
                } else if (11d <= Double.parseDouble(weekInfo.getTmp().get(i)) && Double.parseDouble(weekInfo.getTmp().get(i)) <= 21d) {
                    getTemp.add("70");
                } else if (8d <= Double.parseDouble(weekInfo.getTmp().get(i)) && Double.parseDouble(weekInfo.getTmp().get(i)) <= 24d) {
                    getTemp.add("40");
                } else {
                    getTemp.add("10");
                }
                break;
            //겨울
            case "12":
            case "01":
            case "02":
                if (Double.parseDouble(weekInfo.getTmp().get(i)) >= 2d) {
                    getTemp.add("100");
                } else if (Double.parseDouble(weekInfo.getTmp().get(i)) >= -1d) {
                    getTemp.add("70");
                } else if (Double.parseDouble(weekInfo.getTmp().get(i)) >= -4) {
                    getTemp.add("40");
                } else {
                    getTemp.add("10");
                }
                break;
        }
    }
}
