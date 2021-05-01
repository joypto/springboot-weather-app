package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.entity.DayInfo;
import com.weather.weatherdataapi.model.entity.WeekInfo;
import com.weather.weatherdataapi.model.entity.Region;
import com.weather.weatherdataapi.repository.DayInfoRepository;
import com.weather.weatherdataapi.repository.WeekInfoRepository;
import com.weather.weatherdataapi.util.openapi.weatherGather.WeatherGatherApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class OpenApiService {
    private final WeatherGatherApi weatherGatherApi;
    private final WeekInfoRepository weekInfoRepository;
    private final DayInfoRepository dayInfoRepository;

    public void callApi(CoordinateDto requestDto, ReverseGeocodingResponseDto region, Region wantRegion) {
        try {
            LocalDate currentDate = LocalDate.now();
            System.out.println(currentDate.toString());
            // 해당지역이 이전에 검색이 된적이있으면 이전값 반환
            if (wantRegion.getWeekInfo() != null) {
               if(wantRegion.getWeekInfo().getModifiedAt().toString().equals(currentDate.toString())){
                   System.out.println("값이 존재하지만 아직 업데이트 시간은 아님");
                   return;
               }
                System.out.println("값은 존재하지만 업데이트가 필요함");
            }

            // 검색이 처음된것이면 값 가져오기
                System.out.println("값을 불러오는 중");
                JSONObject jObj;
                JSONObject jObj1;
                JSONArray jObj2;
                JSONObject jObj2b;
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObj = (JSONObject) jsonParser.parse(weatherGatherApi.callWeather(requestDto));

                List<String> tmp = new ArrayList<>();
                List<String> maxTmp = new ArrayList<>();
                List<String> minTmp = new ArrayList<>();
                List<String> humidity = new ArrayList<>();
                List<String> weather = new ArrayList<>();
                List<String> weatherDes = new ArrayList<>();
                List<String> rainPer = new ArrayList<>();
                List<String> rain = new ArrayList<>();
                List<String> hour_tmp = new ArrayList<>();
                List<String> hour_weather = new ArrayList<>();
                List<String> hour_weatherDes = new ArrayList<>();
                List<String> hour_rainPer = new ArrayList<>();
                List<String> hour_time = new ArrayList<>();
                List<String> windSpeed = new ArrayList<>();

                // 주간 날씨 파씽
                JSONArray array = (JSONArray) jsonObj.get("daily");
                for (int i = 0; i < array.size(); i++) {
                    jObj = (JSONObject) array.get(i);
                    jObj1 = (JSONObject) jObj.get("temp");
                    tmp.add(jObj1.get("day").toString());
                    maxTmp.add(jObj1.get("max").toString());
                    minTmp.add(jObj1.get("min").toString());
                    humidity.add(jObj.get("humidity").toString());
                    rainPer.add(jObj.get("pop").toString());
                    windSpeed.add(jObj.get("wind_speed").toString());
                    jObj2 = (JSONArray) jObj.get("weather");
                    jObj2b = (JSONObject) jObj2.get(0);
                    weather.add(jObj2b.get("main").toString());
                    weatherDes.add(jObj2b.get("description").toString());
                }
                WeekInfo weekInfo = WeekInfo.builder()
                        .maxTmp(maxTmp)
                        .minTmp(minTmp)
                        .tmp(tmp)
                        .humidity(humidity)
                        .weather(weather)
                        .weatherDes(weatherDes)
                        .rainPer(rainPer)
                        .rain(rain)
                        .windSpeed(windSpeed)
                        .build();

                // 파싱한 값 저장하고 매핑하기
                wantRegion.updateWeekInfo(weekInfo);
                weekInfo.setRegion(wantRegion);
                weekInfoRepository.save(weekInfo);

                // 하루 시간별 날씨 파싱
                array = (JSONArray) jsonObj.get("hourly");

                for (int i = 0; i < 48; i++) {
                    jObj = (JSONObject) array.get(i);
                    // 기온
                    hour_tmp.add(jObj.get("temp").toString());
                    // unix 타임을 datetime으로 변환
                    long t = Long.parseLong(jObj.get("dt").toString()+"000");
                    SimpleDateFormat date = new SimpleDateFormat("MM dd HH",Locale.KOREA);
                    // 시간
                    hour_time.add(date.format(t));
                    // 강수확률
                    hour_rainPer.add(jObj.get("pop").toString());
                    jObj2 = (JSONArray) jObj.get("weather");
                    jObj2b = (JSONObject) jObj2.get(0);
                    // 날씨
                    hour_weather.add(jObj2b.get("main").toString());
                    // 날씨 설명
                    hour_weatherDes.add(jObj2b.get("description").toString());
                }
                DayInfo dayInfo = DayInfo.builder()
                        .rainPer(hour_rainPer)
                        .tmp(hour_tmp)
                        .weather(hour_weather)
                        .weatherDes(hour_weatherDes)
                        .dailyTime(hour_time)
                        .build();

                // 파싱한 값 저장, 매핑
                wantRegion.updateDayInfo(dayInfo);
                dayInfo.setRegion(wantRegion);
                dayInfoRepository.save(dayInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ScoreResultResponseDto weekInfoConvertToScore(ScoreResultResponseDto scoreResultResponseDto, Region region){
        // 날짜별 환산점수 변환 시작
        List<String> getRainPer = new ArrayList<>();
        List<String> getWeather = new ArrayList<>();
        List<String> getWind = new ArrayList<>();
        List<String> getHumidity = new ArrayList<>();

        for(int i=0; i<region.getWeekInfo().getWeather().size(); i++){
            // 날짜별 기온 변환점수

            // 날짜별 강수확률 변환점수
            if (Double.parseDouble(region.getWeekInfo().getRainPer().get(i))<= 0.2d) {
                getRainPer.add("100");
            }else if(Double.parseDouble(region.getWeekInfo().getRainPer().get(i) )<=0.5d){
                getRainPer.add("70");
            }else if(Double.parseDouble(region.getWeekInfo().getRainPer().get(i) )<=0.7d){
                getRainPer.add("40");
            }else{
                getRainPer.add("10");
            }

            // 날짜별 바람속도 변환점수
            if (Double.parseDouble(region.getWeekInfo().getWindSpeed().get(i))<= 3.3d) {
                getWind.add("100");
            }else if(Double.parseDouble(region.getWeekInfo().getWindSpeed().get(i) )<=5.4d){
                getWind.add("70");
            }else if(Double.parseDouble(region.getWeekInfo().getWindSpeed().get(i) )<=10.7d){
                getWind.add("40");
            }else{
                getWind.add("10");
            }

            // 날짜별 습도 변환점수
            if (40d<=Double.parseDouble(region.getWeekInfo().getHumidity().get(i)) && Double.parseDouble(region.getWeekInfo().getHumidity().get(i))<= 60d) {
                getHumidity.add("100");
            }else if(30d<=Double.parseDouble(region.getWeekInfo().getHumidity().get(i)) && Double.parseDouble(region.getWeekInfo().getHumidity().get(i))<= 70d){
                getHumidity.add("70");
            }else if(20d<=Double.parseDouble(region.getWeekInfo().getHumidity().get(i)) && Double.parseDouble(region.getWeekInfo().getHumidity().get(i))<= 80d){
                getHumidity.add("40");
            }else{
                getHumidity.add("10");
            }

            switch (region.getWeekInfo().getWeatherDes().get(i)){
                case "clear sky":
                case "few clouds":
                    getWeather.add("100");
                    break;
                case "scattered clouds":
                case "broken clouds":
                    getWeather.add("70");
                    break;
                case "shower rain":
                case "rain":
                case "snow":
                    getWeather.add("40");
                    break;
                case "thunderstorm":
                case "mist":
                    getWeather.add("10");
                    break;

            }
        }

        scoreResultResponseDto.setRainPerResult(getRainPer);
        scoreResultResponseDto.setWeatherResult(getWeather);
        scoreResultResponseDto.setHumidityResult(getHumidity);
        scoreResultResponseDto.setWindResult(getWind);

       

        return scoreResultResponseDto;
    }
}
