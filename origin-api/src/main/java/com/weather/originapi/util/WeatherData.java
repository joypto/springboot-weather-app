package com.weather.originapi.util;

import com.weather.originapi.model.dto.RegionResponseDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class WeatherData {

    @PostConstruct
    public RegionResponseDto weatherDataForScore() throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://15.165.235.197:8080/api/weather/data");
        urlBuilder.append("?" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode("37.6027", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode("126.9291", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String data = sb.toString();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(data);
        JSONObject livingHealthWeather = (JSONObject) jsonObject.get("livingHealthWeather");
        String uvToday = (String) livingHealthWeather.get("uvToday");

        RegionResponseDto regionResponseDto = new RegionResponseDto();
        // 일단 오늘 지수만 파싱을 해보았습니다.
        // 여기서 regionResponseDto 에 기상 데이터 값들을 세팅 해놓습니다.
        // 작업하기 전에 먼저 RegionResponseDto 에 필드를 잊지 말고 추가해주세요!
        regionResponseDto.setUvToday(uvToday); // 자외선 지수
        regionResponseDto.setAsthmaToday(asthmaToday); // 천식폐질환지수
        regionResponseDto.setColdToday(coldToday); // 감기가능지수
        regionResponseDto.setPollenRiskToday(pollenRiskToday); // 꽃가루농도지수
        regionResponseDto.setFoodPoisonToday(foodPoisonToday); // 식중독가능지수

        return regionResponseDto;
    }
}