package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.responsedto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.WeatherDataResponseDto;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.WeatherDayInfo;
import com.weather.weatherdataapi.model.entity.info.WeatherWeekInfo;
import com.weather.weatherdataapi.repository.info.WeatherDayInfoRepository;
import com.weather.weatherdataapi.repository.info.WeatherWeekInfoRepository;
import com.weather.weatherdataapi.util.openapi.weather.WeatherApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class WeatherService {
    private final WeatherApi weatherGatherApi;
    private final WeatherWeekInfoRepository weekInfoRepository;

    public void setInfoAndScore(SmallRegion wantRegion, ScoreResultResponseDto scoreResultResponseDto, WeatherDataResponseDto weatherDataResponseDto) throws IOException {
        try {
            LocalDate currentDate = LocalDate.now();

            // 해당지역이 이전에 검색이 된적이있으면 이전값 반환
            if (wantRegion.getWeatherWeekInfoList().get(0).getCreatedAt().toString().equals(currentDate.toString())) {
                System.out.println("주간 날씨 정보가 존재하지만 아직 업데이트 시간은 아님");
                weatherDataResponseDto.setWeekInfo(wantRegion.getWeatherWeekInfoList().get(0));
                weatherDataResponseDto.setDayInfo(wantRegion.getWeatherDayInfoList().get(0));
                convertInfoToScore(scoreResultResponseDto, wantRegion);
                return;
            }
            System.out.println("주간 날씨 정보가 존재 하지만 업데이트가 필요함");

            weatherGatherApi.callWeather(wantRegion,weatherDataResponseDto);
            convertInfoToScore(scoreResultResponseDto, wantRegion);
        } catch (Exception e) {
            System.out.println("값이 존재하지 않음");
            weatherGatherApi.callWeather(wantRegion,weatherDataResponseDto);
            convertInfoToScore(scoreResultResponseDto, wantRegion);
        }
    }


    public void convertInfoToScore(ScoreResultResponseDto scoreResultResponseDto, SmallRegion smallRegion) {
        // 날짜별 환산점수 변환 시작
        List<String> getRainPer = new ArrayList<>();
        List<String> getWeather = new ArrayList<>();
        List<String> getWind = new ArrayList<>();
        List<String> getHumidity = new ArrayList<>();
        List<String> getTemp = new ArrayList<>();

        String getMonth = smallRegion.getWeatherDayInfoList().get(0).getDailyTime().get(0).substring(0, 2);
        System.out.println(getMonth);
        System.out.println(smallRegion.getWeatherWeekInfoList().size());
        System.out.println(smallRegion.getWeatherWeekInfoList().get(0).toString());
        for (int i = 0; i < 7; i++) {
            getRainScore(smallRegion, getRainPer, i);
            getWindScore(smallRegion, getWind, i);
            getHumidityScore(smallRegion, getHumidity, i);
            getWeatherScore(smallRegion, getWeather, i);
            getWeatherScore(smallRegion,getTemp,i,getMonth);
        }

        scoreResultResponseDto.setRainPerResult(getRainPer);
        scoreResultResponseDto.setWeatherResult(getWeather);
        scoreResultResponseDto.setHumidityResult(getHumidity);
        scoreResultResponseDto.setWindResult(getWind);
        scoreResultResponseDto.setTempResult(getTemp);
    }

    public void getRainScore(SmallRegion smallRegion, List<String> getRainPer, int i) {
        // 날짜별 강수확률 변환점수
        if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getRainPer().get(i)) <= 0.2d) {
            getRainPer.add("100");
        } else if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getRainPer().get(i)) <= 0.5d) {
            getRainPer.add("70");
        } else if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getRainPer().get(i)) <= 0.7d) {
            getRainPer.add("40");
        } else {
            getRainPer.add("10");
        }
    }

    public void getWindScore(SmallRegion smallRegion, List<String> getWind, int i) {
        if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getWindSpeed().get(i)) <= 3.3d) {
            getWind.add("100");
        } else if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getWindSpeed().get(i)) <= 5.4d) {
            getWind.add("70");
        } else if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getWindSpeed().get(i)) <= 10.7d) {
            getWind.add("40");
        } else {
            getWind.add("10");
        }
    }

    public void getHumidityScore(SmallRegion smallRegion, List<String> getHumidity, int i) {
        if (40d <= Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getHumidity().get(i)) && Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getHumidity().get(i)) <= 60d) {
            getHumidity.add("100");
        } else if (30d <= Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getHumidity().get(i)) && Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getHumidity().get(i)) <= 70d) {
            getHumidity.add("70");
        } else if (20d <= Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getHumidity().get(i)) && Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getHumidity().get(i)) <= 80d) {
            getHumidity.add("40");
        } else {
            getHumidity.add("10");
        }
    }

    public void getWeatherScore(SmallRegion smallRegion, List<String> getWeather, int i) {
        switch (smallRegion.getWeatherWeekInfoList().get(0).getWeatherIcon().get(i)) {
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
    public void getWeatherScore(SmallRegion smallRegion, List<String> getTemp, int i,String getMonth) {
        switch (getMonth) {
            // 봄
            case "03":
            case "04":
            case "05":
                if (18d <= Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) && Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) <= 22d) {
                    getTemp.add("100");
                } else if (15d <= Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) && Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) <= 25d) {
                    getTemp.add("70");
                } else if (12d <= Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) && Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) <= 28d) {
                    getTemp.add("40");
                } else {
                    getTemp.add("10");
                }
                break;
            //여름
            case "06":
            case "07":
            case "08":
                if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) <= 24d) {
                    getTemp.add("100");
                } else if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) <= 27d) {
                    getTemp.add("70");
                } else if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) <= 30d) {
                    getTemp.add("40");
                } else {
                    getTemp.add("10");
                }
                break;
            //가을
            case "09":
            case "10":
            case "11":
                if (14d <= Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) && Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) <= 18d) {
                    getTemp.add("100");
                } else if (11d <= Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) && Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) <= 21d) {
                    getTemp.add("70");
                } else if (8d <= Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) && Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) <= 24d) {
                    getTemp.add("40");
                } else {
                    getTemp.add("10");
                }
                break;
            //겨울
            case "12":
            case "01":
            case "02":
                if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) >= 2d) {
                    getTemp.add("100");
                } else if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) >= -1d) {
                    getTemp.add("70");
                } else if (Double.parseDouble(smallRegion.getWeatherWeekInfoList().get(0).getTmp().get(i)) >= -4) {
                    getTemp.add("40");
                } else {
                    getTemp.add("10");
                }
                break;
        }
    }
}
