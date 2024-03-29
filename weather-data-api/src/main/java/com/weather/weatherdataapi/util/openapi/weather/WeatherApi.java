package com.weather.weatherdataapi.util.openapi.weather;

import com.weather.weatherdataapi.exception.repository.info.InvalidWeatherDayInfoException;
import com.weather.weatherdataapi.exception.repository.info.InvalidWeatherWeekInfoException;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.WeatherDayInfo;
import com.weather.weatherdataapi.model.entity.info.WeatherWeekInfo;
import com.weather.weatherdataapi.model.vo.redis.WeatherDayRedisVO;
import com.weather.weatherdataapi.model.vo.redis.WeatherWeekRedisVO;
import com.weather.weatherdataapi.repository.info.WeatherDayInfoRepository;
import com.weather.weatherdataapi.repository.info.WeatherWeekInfoRepository;
import com.weather.weatherdataapi.repository.redis.WeatherDayRedisRepository;
import com.weather.weatherdataapi.repository.redis.WeatherWeekRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherApi {
    private final WeatherWeekInfoRepository weekInfoRepository;
    private final WeatherDayInfoRepository dayInfoRepository;
    private final WeatherWeekRedisRepository weatherWeekRedisRepository;
    private final WeatherDayRedisRepository weatherDayRedisRepository;

    public WeatherWeekInfo callWeather(SmallRegion wantRegion, TotalDataResponseDto weatherDataResponseDto) throws IOException {
        try {
            String lat = wantRegion.getLatitude();
            String lon = wantRegion.getLongitude();
            return initData(useApi(lat, lon), wantRegion, weatherDataResponseDto);
        } catch (Exception e) {
            String lat = "37.57037778";
            String lon = "126.9816417";
            log.info("지역정보 오류: 서울시 종로구로 대체 비상비상비상비상!!!!!!!");
            return initData(useApi(lat, lon), wantRegion, weatherDataResponseDto);
        }
    }

    public String useApi(String lat, String lon) throws IOException {
        StringBuilder result = new StringBuilder();
        String urlStr = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude=minutely,current&appid=0479c3d98eb03e0a92d9a69ce53b631f&units=metric";
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            result.append(returnLine + "\n\r");
        }

        urlConnection.disconnect();
        return result.toString();
    }

    public WeatherWeekInfo initData(String jsonData, SmallRegion wantRegion, TotalDataResponseDto weatherDataResponseDto) {
        try {
            log.info("날씨 정보와 시간 정보 요청");
            JSONObject jObj;
            JSONObject jObj1;
            JSONArray jObj2;
            JSONObject jObj2b;
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonData);

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
            List<String> hour_time_dump = new ArrayList<>();
            List<String> windSpeed = new ArrayList<>();
            List<String> weatherIcon = new ArrayList<>();
            List<String> hour_weatherIcon = new ArrayList<>();

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
                weatherIcon.add(jObj2b.get("icon").toString());
            }
            WeatherWeekInfo weekInfo = WeatherWeekInfo.builder()
                    .maxTmp(maxTmp)
                    .minTmp(minTmp)
                    .tmp(tmp)
                    .humidity(humidity)
                    .weather(weather)
                    .weatherDes(weatherDes)
                    .rainPer(rainPer)
                    .rain(rain)
                    .windSpeed(windSpeed)
                    .weatherIcon(weatherIcon)
                    .build();

            // 파싱한 값 저장하고 매핑하기
            weekInfo.setSmallRegion(wantRegion);
            weekInfoRepository.save(weekInfo);
            ;
            weatherDataResponseDto.setWeekInfo(weekInfo);
            WeatherWeekRedisVO weekCache = new WeatherWeekRedisVO(weekInfo,18000);
            weatherWeekRedisRepository.save(weekCache);

            // 하루 시간별 날씨 파싱
            array = (JSONArray) jsonObj.get("hourly");
            for (int i = 0; i < 48; i++) {
                jObj = (JSONObject) array.get(i);
                // 기온
                hour_tmp.add(jObj.get("temp").toString());
                // unix 타임을 datetime으로 변환
                long t = Long.parseLong(jObj.get("dt").toString() + "000");
                SimpleDateFormat date = new SimpleDateFormat("MM dd HH", Locale.KOREA);
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
                hour_weatherIcon.add(jObj2b.get("icon").toString());
            }
            hour_time_dump.add(hour_time.get(0));

            WeatherDayInfo dayInfo = WeatherDayInfo.builder()
                    .rainPer(hour_rainPer)
                    .tmp(hour_tmp)
                    .weather(hour_weather)
                    .weatherDes(hour_weatherDes)
                    .dailyTime(hour_time_dump)
                    .weatherIcon(hour_weatherIcon)
                    .build();

            // 파싱한 값 저장, 매핑
            dayInfo.setSmallRegion(wantRegion);
            dayInfoRepository.save(dayInfo);
            dayInfo.setDailyTime(hour_time);
            weatherDataResponseDto.setDayInfo(dayInfo);
            WeatherDayRedisVO dayCache = new WeatherDayRedisVO(dayInfo,18000);
            weatherDayRedisRepository.save(dayCache);
            return weekInfo;
        } catch (Exception e) {
            log.info("파싱에 실패했습니다. db에 있는 최신 정보로 대체합니다");
            WeatherWeekInfo weekInfo = weekInfoRepository.findFirstBySmallRegionOrderByCreatedAtDesc(wantRegion).orElseThrow(() -> new InvalidWeatherWeekInfoException());
            WeatherDayInfo dayInfo = dayInfoRepository.findFirstBySmallRegionOrderByCreatedAtDesc(wantRegion).orElseThrow(() -> new InvalidWeatherDayInfoException());
            weatherDataResponseDto.setWeekInfo(weekInfo);
            weatherDataResponseDto.setDayInfo(dayInfo);
            return weekInfo;
        }
    }
}