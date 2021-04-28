package com.weather.weatherdataapi.util.openapi.livinghealthweather;

import com.weather.weatherdataapi.model.entity.LivingHealthWeather;
import com.weather.weatherdataapi.repository.LivingHealthWeatherRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
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
import java.util.List;

@Component
@RequiredArgsConstructor
public class HealthWeatherApiCall {

    private  String URL_ENCODED_SERVICE_KEY = "zhvzvF5vNC7ufu7H%2BQnPJtEQbF2QdNZ0qdvZWLeR%2BnL0UwxwnCgrkmxKB9oqCXVSJp95YTliRHwzxvGdrvjetg%3D%3D";
    private String AREA_NO = "1100000000";
    private String DATE = "2021042706";
    private final LivingHealthWeatherRepository livingHealthWeatherRepository;

    public void healthWeatherApiCall(List<String> address) throws IOException, ParseException {
        System.out.println(address);

        String [] methods = {"HealthWthrIdxService/getAsthmaIdx", "HealthWthrIdxService/getFoodPoisoningIdx", "HealthWthrIdxService/getColdIdx", "HealthWthrIdxService/getOakPollenRiskIdx", "HealthWthrIdxService/getPinePollenRiskIdx", "LivingWthrIdxService01/getUVIdx"};
        LivingHealthWeather livingHealthWeather = new LivingHealthWeather();

        for(int i=0; i<methods.length; i++) {

            String method = methods[i];

            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/" + method); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URL_ENCODED_SERVICE_KEY); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("areaNo", "UTF-8") + "=" + URLEncoder.encode(AREA_NO, "UTF-8")); /*서울지점*/
            urlBuilder.append("&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(DATE, "UTF-8")); /*2017년6월8일6시*/
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

            System.out.println(method);
            System.out.println(today);
            System.out.println(tomorrow);
            System.out.println(theDayAfterTomorrow);

            if (livingHealthWeather.getDate() == null) {
                livingHealthWeather.setDate(date);
            }

            if (livingHealthWeather.getAreaNo() == null) {
                livingHealthWeather.setAreaNo(areaNo);
            }

            if (method == "HealthWthrIdxService/getAsthmaIdx") {
                livingHealthWeather.setAsthmaToday(today);
                livingHealthWeather.setAsthmaTomorrow(tomorrow);
                livingHealthWeather.setAsthmaTheDayAfterTomorrow(theDayAfterTomorrow);
            } else if (method == "HealthWthrIdxService/getOakPollenRiskIdx") {
                livingHealthWeather.setOakPollenRiskToday(today);
                livingHealthWeather.setOakPollenRiskTomorrow(tomorrow);
                livingHealthWeather.setOakPollenRiskTheDayAfterTomorrow(theDayAfterTomorrow);
            } else if (method == "HealthWthrIdxService/getPinePollenRiskIdx") {
                livingHealthWeather.setPinePollenRiskToday(today);
                livingHealthWeather.setPinePollenRiskTomorrow(tomorrow);
                livingHealthWeather.setPinePollenRiskTheDayAfterTomorrow(theDayAfterTomorrow);
            } else if (method == "HealthWthrIdxService/getColdIdx") {
                livingHealthWeather.setColdToday(today);
                livingHealthWeather.setColdTomorrow(tomorrow);
                livingHealthWeather.setColdTheDayAfterTomorrow(theDayAfterTomorrow);
            } else if (method == "HealthWthrIdxService/getFoodPoisoningIdx") {
                livingHealthWeather.setFoodPoisonToday(today);
                livingHealthWeather.setFoodPoisonTomorrow(tomorrow);
                livingHealthWeather.setFoodPoisonTheDayAfterTomorrow(theDayAfterTomorrow);
            } else if (method == "LivingWthrIdxService01/getUVIdx") {
                livingHealthWeather.setUvToday(today);
                livingHealthWeather.setUvTomorrow(tomorrow);
                livingHealthWeather.setFoodPoisonTheDayAfterTomorrow(theDayAfterTomorrow);
            }

        }

        // DB에 저장
        livingHealthWeatherRepository.save(livingHealthWeather);
    }

}
