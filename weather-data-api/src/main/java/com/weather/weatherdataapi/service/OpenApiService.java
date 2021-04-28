package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.WeatherDataRequestDto;
import com.weather.weatherdataapi.model.entity.WeekInfo;
import com.weather.weatherdataapi.repository.WeekInfoRepository;
import com.weather.weatherdataapi.util.openapi.weatherGather.WeatherGatherApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OpenApiService {
    private final WeatherGatherApi weatherGatherApi;
    private final WeekInfoRepository weekInfoRepository;

    public void callApi(WeatherDataRequestDto requestDto) {
        try {
            JSONObject jObj;
            JSONObject jObj1;
            JSONArray jObj2;
            JSONObject jObj2b;
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(weatherGatherApi.callWeather());
            JSONArray array = (JSONArray) jsonObj.get("daily");
            System.out.println(weatherGatherApi.callWeather());
            List<String> tmp = new ArrayList<>();
            List<String> maxTmp = new ArrayList<>();
            List<String> minTmp = new ArrayList<>();
            List<String> humidity = new ArrayList<>();
            List<String> weather = new ArrayList<>();
            List<String> weatherDes = new ArrayList<>();
            List<String> rainPer = new ArrayList<>();
            List<String> rain = new ArrayList<>();

            for (int i = 0; i < array.size(); i++) {
                jObj = (JSONObject) array.get(i);
                jObj1 = (JSONObject) jObj.get("temp");
                tmp.add(jObj1.get("day").toString());
                maxTmp.add(jObj1.get("max").toString());
                minTmp.add(jObj1.get("min").toString());
                humidity.add(jObj.get("humidity").toString());
                rainPer.add(jObj.get("pop").toString());
//                if (jObj.get("rain").toString() != null){
//                    rain.add(jObj.get("rain").toString());
//                }
//                else{
//                    rain.add("");
//                }
//                JSONArray jObj3 = (JSONArray) jObj.get("weather");
//                System.out.println(jObj3.toString());
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

            weekInfoRepository.save(weekInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
