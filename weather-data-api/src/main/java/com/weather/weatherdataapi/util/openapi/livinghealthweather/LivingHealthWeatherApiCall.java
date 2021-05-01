package com.weather.weatherdataapi.util.openapi.livinghealthweather;

import com.weather.weatherdataapi.model.dto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.entity.info.LivingHealthWeather;
import com.weather.weatherdataapi.model.entity.Region;
import com.weather.weatherdataapi.repository.LivingHealthWeatherRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class LivingHealthWeatherApiCall {

    private  String URL_ENCODED_SERVICE_KEY = "zhvzvF5vNC7ufu7H%2BQnPJtEQbF2QdNZ0qdvZWLeR%2BnL0UwxwnCgrkmxKB9oqCXVSJp95YTliRHwzxvGdrvjetg%3D%3D";
    private final LivingHealthWeatherRepository livingHealthWeatherRepository;

    public void livingHealthWeatherApiCall(ReverseGeocodingResponseDto address, Region region) throws IOException, ParseException {

        // 예보 기준일 생성
        String dateResult = "2021042706";
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour >= 6) {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
            Date dateSet = cal.getTime();
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            dateResult = sdf.format(dateSet);
        } else if (hour < 6) {
            Date dDate = new Date();
            dDate = new Date(dDate.getTime()+(1000*60*60*24*-1));
            SimpleDateFormat dSdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
            dateResult = dSdf.format(dDate);
        }

        // 24시간 이내에 요청이 된 시/구 주소인지 검사
        if (region.getLivingHealthWeather() != null && dateResult.toString().equals(region.getLivingHealthWeather().getDate().toString())) {
            return;
        }

        // 해당 시/구 주소를 가진 Region의 행정동 주소 불러오기
        String admcode = region.getAdmCode().toString();

        String [] methods = {"HealthWthrIdxService/getAsthmaIdx", "HealthWthrIdxService/getFoodPoisoningIdx", "HealthWthrIdxService/getOakPollenRiskIdx",  "LivingWthrIdxService01/getUVIdx"};
        LivingHealthWeather livingHealthWeather = new LivingHealthWeather();
        if (region.getLivingHealthWeather() != null) {
            livingHealthWeather = region.getLivingHealthWeather();
        }

        // Region Table 과 LivingWeatherTable @OneToOne Mapping 과정
        region.updateLivingHealthWeather(livingHealthWeather);
        livingHealthWeather.setRegion(region);

        // for 문으로 반복하며 OPEN API에 여러 요청들을 보내는 코드
        for(int i=0; i<methods.length; i++) {

            String method = methods[i];
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/" + method); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URL_ENCODED_SERVICE_KEY); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("areaNo", "UTF-8") + "=" + URLEncoder.encode(admcode, "UTF-8")); /*서울지점*/
            urlBuilder.append("&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(dateResult + "06", "UTF-8")); /*2017년6월8일6시*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml, json 선택(미입력시 xml)*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            // System.out.println("Response code: " + conn.getResponseCode());
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
            String areaNo = (String) itemObject.get("areaNo");
            String today = (String) itemObject.get("today");
            String tomorrow = (String) itemObject.get("tomorrow");
            String theDayAfterTomorrow = (String) itemObject.get("theDayAfterTomorrow");

            livingHealthWeather.setDate(date);

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
            } else if (method == "HealthWthrIdxService/getFoodPoisoningIdx") {
                livingHealthWeather.setFoodPoisonToday(today);
                livingHealthWeather.setFoodPoisonTomorrow(tomorrow);
                livingHealthWeather.setFoodPoisonTheDayAfterTomorrow(theDayAfterTomorrow);
            } else if (method == "LivingWthrIdxService01/getUVIdx") {
                livingHealthWeather.setUvToday(today);
                livingHealthWeather.setUvTomorrow(tomorrow);
                livingHealthWeather.setUvTheDayAfterTomorrow(theDayAfterTomorrow);
            }
        }

        // DB에 저장
        livingHealthWeatherRepository.save(livingHealthWeather);

    }

}
