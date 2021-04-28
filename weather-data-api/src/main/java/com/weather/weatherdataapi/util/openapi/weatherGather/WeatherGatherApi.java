package com.weather.weatherdataapi.util.openapi.weatherGather;


import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class WeatherGatherApi {
    public String callWeather() throws IOException{
        StringBuilder result = new StringBuilder();

        String urlStr= "https://api.openweathermap.org/data/2.5/onecall?lat=37.532600&lon=127.024612&exclude=hourly,minutely,current&appid=0479c3d98eb03e0a92d9a69ce53b631f&units=metric";
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