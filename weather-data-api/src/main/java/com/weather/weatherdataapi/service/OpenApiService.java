package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.WeatherDataRequestDto;
import com.weather.weatherdataapi.model.entity.DayInfo;
import com.weather.weatherdataapi.model.entity.WeekInfo;
import com.weather.weatherdataapi.model.entity.region.Region;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class OpenApiService {
    private final WeatherGatherApi weatherGatherApi;
    private final WeekInfoRepository weekInfoRepository;
    private final DayInfoRepository dayInfoRepository;

    public void callApi(WeatherDataRequestDto requestDto, ReverseGeocodingResponseDto region, Region wantRegion) {
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
}
