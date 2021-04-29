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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OpenApiService {
    private final WeatherGatherApi weatherGatherApi;
    private final WeekInfoRepository weekInfoRepository;
    private final DayInfoRepository dayInfoRepository;

    public void callApi(WeatherDataRequestDto requestDto, ReverseGeocodingResponseDto region, Region wantRegion) {
        try {
            int currentDate = LocalDate.now().getDayOfMonth();

            if (wantRegion.getWeekInfo() != null) {
                System.out.println("값 존재 다시 불러올 필요 없음");
                return;
            } else {
                System.out.println("값이 없어서 값을 불러오는 중");
                JSONObject jObj;
                JSONObject jObj1;
                JSONArray jObj2;
                JSONObject jObj2b;
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObj = (JSONObject) jsonParser.parse(weatherGatherApi.callWeather(requestDto));
                JSONArray array = (JSONArray) jsonObj.get("daily");
                List<String> tmp = new ArrayList<>();
                List<String> maxTmp = new ArrayList<>();
                List<String> minTmp = new ArrayList<>();
                List<String> humidity = new ArrayList<>();
                List<String> weather = new ArrayList<>();
                List<String> weatherDes = new ArrayList<>();
                List<String> rainPer = new ArrayList<>();
                List<String> rain = new ArrayList<>();
                String big_region = region.getBigRegion();
                String small_region = region.getSmallRegion();
                if (small_region.equals("")) {
                    small_region = region.getBigRegion();
                }

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
                        .big_region(big_region)
                        .small_region(small_region)
                        .maxTmp(maxTmp)
                        .minTmp(minTmp)
                        .tmp(tmp)
                        .humidity(humidity)
                        .weather(weather)
                        .weatherDes(weatherDes)
                        .rainPer(rainPer)
                        .rain(rain)
                        .build();


                wantRegion.updateWeekInfo(weekInfo);
                weekInfo.setRegion(wantRegion);
                weekInfoRepository.save(weekInfo);
                array = (JSONArray) jsonObj.get("hourly");
                tmp.clear();
                weather.clear();
                weatherDes.clear();
                rainPer.clear();
                for (int i = 0; i < 24; i++) {
                    jObj = (JSONObject) array.get(i);
                    tmp.add(jObj.get("temp").toString());
                    rainPer.add(jObj.get("pop").toString());
                    jObj2 = (JSONArray) jObj.get("weather");
                    jObj2b = (JSONObject) jObj2.get(0);
                    weather.add(jObj2b.get("main").toString());
                    weatherDes.add(jObj2b.get("description").toString());
                }
                DayInfo dayInfo = DayInfo.builder()
                        .rainPer(rainPer)
                        .tmp(tmp)
                        .weather(weather)
                        .weatherDes(weatherDes)
                        .build();
                wantRegion.updateDayInfo(dayInfo);
                dayInfoRepository.save(dayInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
