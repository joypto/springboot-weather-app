package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.ScoreResultResponseDto;
import com.weather.weatherdataapi.model.entity.info.WeatherDayInfo;
import com.weather.weatherdataapi.model.entity.info.WeatherWeekInfo;
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
public class WeatherService {
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
                   System.out.println("주간 날씨 정보가 존재하지만 아직 업데이트 시간은 아님");
                   return;
               }
                System.out.println("주간 날씨 정보가 존재 하지만 업데이트가 필요함");
            }

            // 검색이 처음된것이면 값 가져오기
                System.out.println("주간 날씨 정보와 시간 정보를 불러오는 중");
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
                    hour_weatherIcon.add(jObj2b.get("icon").toString());
                }
                WeatherDayInfo dayInfo = WeatherDayInfo.builder()
                        .rainPer(hour_rainPer)
                        .tmp(hour_tmp)
                        .weather(hour_weather)
                        .weatherDes(hour_weatherDes)
                        .dailyTime(hour_time)
                        .weatherIcon(hour_weatherIcon)
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
        List<String> getTemp = new ArrayList<>();
        String getMonth = region.getDayInfo().getDailyTime().get(0).substring(0,2);
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

            // 날짜별 날씨 변환 점수
            switch (region.getWeekInfo().getWeatherIcon().get(i)){
                case "01d":
                case "01n":
                case "02d":
                case "02n":
                    getWeather.add("100");
                    break;
                case "03d":
                case "03n":
                case "04d":
                case "04n":
                    getWeather.add("70");
                    break;
                case "09d":
                case "09n":
                case "10d":
                case "10n":
                case "13d":
                case "13n":
                    getWeather.add("40");
                    break;
                case "11d":
                case "11n":
                case "50d":
                case "50n":
                    getWeather.add("10");
                    break;

            }

            switch (getMonth){
                // 봄
                case "03":
                case "04":
                case "05":
                    if (18d<=Double.parseDouble(region.getWeekInfo().getTmp().get(i)) && Double.parseDouble(region.getWeekInfo().getTmp().get(i))<= 22d) {
                        getTemp.add("100");
                    }else if(15d<=Double.parseDouble(region.getWeekInfo().getTmp().get(i)) && Double.parseDouble(region.getWeekInfo().getTmp().get(i))<= 25d){
                        getTemp.add("70");
                    }else if(12d<=Double.parseDouble(region.getWeekInfo().getTmp().get(i)) && Double.parseDouble(region.getWeekInfo().getTmp().get(i))<= 28d){
                        getTemp.add("40");
                    }else{
                        getTemp.add("10");
                    }
                    break;
                //여름
                case "06":
                case "07":
                case "08":
                    if (Double.parseDouble(region.getWeekInfo().getTmp().get(i))<= 24d) {
                        getTemp.add("100");
                    }else if(Double.parseDouble(region.getWeekInfo().getTmp().get(i))<= 27d){
                        getTemp.add("70");
                    }else if(Double.parseDouble(region.getWeekInfo().getTmp().get(i))<= 30d){
                        getTemp.add("40");
                    }else{
                        getTemp.add("10");
                    }
                    break;
                //가을
                case "09":
                case "10":
                case "11":
                    if (14d<=Double.parseDouble(region.getWeekInfo().getTmp().get(i)) && Double.parseDouble(region.getWeekInfo().getTmp().get(i))<= 18d) {
                        getTemp.add("100");
                    }else if(11d<=Double.parseDouble(region.getWeekInfo().getTmp().get(i)) && Double.parseDouble(region.getWeekInfo().getTmp().get(i))<= 21d){
                        getTemp.add("70");
                    }else if(8d<=Double.parseDouble(region.getWeekInfo().getTmp().get(i)) && Double.parseDouble(region.getWeekInfo().getTmp().get(i))<= 24d){
                        getTemp.add("40");
                    }else{
                        getTemp.add("10");
                    }
                    break;
                //겨울
                case "12":
                case "01":
                case "02":
                    if (Double.parseDouble(region.getWeekInfo().getTmp().get(i))>= 2d) {
                        getTemp.add("100");
                    }else if(Double.parseDouble(region.getWeekInfo().getTmp().get(i))>=-1d){
                        getTemp.add("70");
                    }else if(Double.parseDouble(region.getWeekInfo().getTmp().get(i))>=-4){
                        getTemp.add("40");
                    }else{
                        getTemp.add("10");
                    }
                    break;
            }

        }


        scoreResultResponseDto.setRainPerResult(getRainPer);
        scoreResultResponseDto.setWeatherResult(getWeather);
        scoreResultResponseDto.setHumidityResult(getHumidity);
        scoreResultResponseDto.setWindResult(getWind);
        scoreResultResponseDto.setTempResult(getTemp);
        return scoreResultResponseDto;
    }
}
