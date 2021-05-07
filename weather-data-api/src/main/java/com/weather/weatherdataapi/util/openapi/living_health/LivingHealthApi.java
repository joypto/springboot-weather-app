package com.weather.weatherdataapi.util.openapi.living_health;

import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import com.weather.weatherdataapi.repository.info.LivingHealthInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Slf4j
@Component
@RequiredArgsConstructor
public class LivingHealthApi {

    private final LivingHealthInfoRepository livingHealthInfoRepository;
    private String URL_ENCODED_SERVICE_KEY = "zhvzvF5vNC7ufu7H%2BQnPJtEQbF2QdNZ0qdvZWLeR%2BnL0UwxwnCgrkmxKB9oqCXVSJp95YTliRHwzxvGdrvjetg%3D%3D";

    public LivingHealthInfo livingHealthApi(BigRegion bigRegion, String admCode) throws IOException, ParseException {

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
            dDate = new Date(dDate.getTime() + (1000 * 60 * 60 * 24 * -1));
            SimpleDateFormat dSdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
            dateResult = dSdf.format(dDate);
        }

        // 해당 시/구 주소를 가진 Region의 행정동 주소 불러오기
        String admcode = admCode;

        String[] methods = {"HealthWthrIdxService/getAsthmaIdx", "HealthWthrIdxService/getFoodPoisoningIdx", "HealthWthrIdxService/getOakPollenRiskIdx", "LivingWthrIdxService01/getUVIdx"};
        LivingHealthInfo livingHealthInfo = new LivingHealthInfo();

        // for 문으로 반복하며 OPEN API에 여러 요청들을 보내는 코드
        for (int i = 0; i < methods.length; i++) {

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

            try {
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

                livingHealthInfo.setDate(date);

                if (livingHealthInfo.getAreaNo() == null) {
                    livingHealthInfo.setAreaNo(areaNo);
                }

                if (method == "HealthWthrIdxService/getAsthmaIdx") {
                    livingHealthInfo.setAsthmaToday(today);
                    livingHealthInfo.setAsthmaTomorrow(tomorrow);
                    livingHealthInfo.setAsthmaTheDayAfterTomorrow(theDayAfterTomorrow);
                } else if (method == "HealthWthrIdxService/getOakPollenRiskIdx") {
                    livingHealthInfo.setOakPollenRiskToday(today);
                    livingHealthInfo.setOakPollenRiskTomorrow(tomorrow);
                    livingHealthInfo.setOakPollenRiskTheDayAfterTomorrow(theDayAfterTomorrow);
                } else if (method == "HealthWthrIdxService/getFoodPoisoningIdx") {
                    livingHealthInfo.setFoodPoisonToday(today);
                    livingHealthInfo.setFoodPoisonTomorrow(tomorrow);
                    livingHealthInfo.setFoodPoisonTheDayAfterTomorrow(theDayAfterTomorrow);
                } else if (method == "LivingWthrIdxService01/getUVIdx") {
                    livingHealthInfo.setUvToday(today);
                    livingHealthInfo.setUvTomorrow(tomorrow);
                    livingHealthInfo.setUvTheDayAfterTomorrow(theDayAfterTomorrow);
                }
            } catch (ParseException e) {
                log.info("생활보건기상지수 OPEN API 정보 파싱에 실패했습니다.");
                LivingHealthInfo yesterdayInfo = livingHealthInfoRepository.findSecondByAreaNoOrderByCreatedAtDesc(admcode);
                if (method == "HealthWthrIdxService/getAsthmaIdx") {
                    livingHealthInfo.setAsthmaToday(yesterdayInfo.getAsthmaToday());
                    livingHealthInfo.setAsthmaTomorrow(yesterdayInfo.getAsthmaTomorrow());
                    livingHealthInfo.setAsthmaTheDayAfterTomorrow(yesterdayInfo.getAsthmaTheDayAfterTomorrow());
                } else if (method == "HealthWthrIdxService/getOakPollenRiskIdx") {
                    livingHealthInfo.setOakPollenRiskToday(yesterdayInfo.getOakPollenRiskToday());
                    livingHealthInfo.setOakPollenRiskTomorrow(yesterdayInfo.getOakPollenRiskTomorrow());
                    livingHealthInfo.setOakPollenRiskTheDayAfterTomorrow(yesterdayInfo.getOakPollenRiskTheDayAfterTomorrow());
                } else if (method == "HealthWthrIdxService/getFoodPoisoningIdx") {
                    livingHealthInfo.setFoodPoisonToday(yesterdayInfo.getFoodPoisonToday());
                    livingHealthInfo.setFoodPoisonTomorrow(yesterdayInfo.getFoodPoisonTomorrow());
                    livingHealthInfo.setFoodPoisonTheDayAfterTomorrow(yesterdayInfo.getFoodPoisonTheDayAfterTomorrow());
                } else if (method == "LivingWthrIdxService01/getUVIdx") {
                    livingHealthInfo.setUvToday(yesterdayInfo.getUvToday());
                    livingHealthInfo.setUvTomorrow(yesterdayInfo.getUvTomorrow());
                    livingHealthInfo.setUvTheDayAfterTomorrow(yesterdayInfo.getUvTheDayAfterTomorrow());
                }
                log.info("데이터베이스에서 전 날의 데이터를 가져와 임시 업로드 완료하였습니다.");
            }
        }

        livingHealthInfo.setBigRegion(bigRegion);
        return livingHealthInfo;
    }

}
