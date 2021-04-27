package com.weather.weatherdataapi.util.openapi.livinghealthweather;

import com.weather.weatherdataapi.model.entity.LivingHealthWeather;
import com.weather.weatherdataapi.repository.LivingHealthWeatherRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;

@Component
@RequiredArgsConstructor
public class LivingWeatherApiCall {

    private String URL_ENCODED_SERVICE_KEY = "zhvzvF5vNC7ufu7H%2BQnPJtEQbF2QdNZ0qdvZWLeR%2BnL0UwxwnCgrkmxKB9oqCXVSJp95YTliRHwzxvGdrvjetg%3D%3D";
    private final LivingHealthWeatherRepository livingHealthWeatherRepository;

//    @PostConstruct
    public void uvIdxApiCall() throws IOException, ParseException {

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/LivingWthrIdxService01/getUVIdx"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URL_ENCODED_SERVICE_KEY); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("areaNo", "UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); /*서울지점*/
        urlBuilder.append("&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode("2021042706", "UTF-8")); /*2017년6월8일6시*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml, json 선택(미입력시 xml)*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
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
        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");
        JSONObject itemObject = (JSONObject) item.get(0);

        // 여기서부터가 사용하는 값!
        String date = (String) itemObject.get("date");
        String code = (String) itemObject.get("code");
        String areaNo = (String) itemObject.get("areaNo");
        String today = (String) itemObject.get("today");
        String tomorrow = (String) itemObject.get("tomorrow");
        String theDayAfterTomorrow = (String) itemObject.get("theDayAfterTomorrow");

        LivingHealthWeather uvIdx = new LivingHealthWeather();
        uvIdx.setDate(date);
        uvIdx.setAreaNo(areaNo);
        uvIdx.setUvToday(today);
        uvIdx.setUvTomorrow(tomorrow);
        uvIdx.setUvTheDayAfterTomorrow(theDayAfterTomorrow);

        // DB에 저장
        livingHealthWeatherRepository.save(uvIdx);

    }

}


