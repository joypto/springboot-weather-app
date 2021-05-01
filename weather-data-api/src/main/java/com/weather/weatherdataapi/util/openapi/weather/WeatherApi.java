package com.weather.weatherdataapi.util.openapi.weather;


import com.weather.weatherdataapi.model.dto.CoordinateDto;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class WeatherApi {
    public String callWeather(CoordinateDto requestDto) throws IOException{
        StringBuilder result = new StringBuilder();
        String lat= requestDto.getLatitude();
        String lon=requestDto.getLongitude();
        String urlStr= "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=minutely,current&appid=0479c3d98eb03e0a92d9a69ce53b631f&units=metric";
        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));

        String returnLine;

        while((returnLine = br.readLine()) != null){
            result.append(returnLine+"\n\r");
        }

        urlConnection.disconnect();
        return result.toString();
    }
}