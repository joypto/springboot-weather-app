package com.weather.weatherdataapi.corona;

import com.weather.weatherdataapi.util.DateTimeUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GovCoronaOpenApi implements ICoronaOpenApi {

    private final String URL_ENCODED_SERVICE_KEY = "iVwYPkC6bU1VAQicYcfS34fOnic5axhMluibhmVlWbQzkTP7YNapHzeMXMzwWzRjXYtTNk9shZRR%2BcveP6daGw%3D%3D";

    public GovCoronaOpenApi() throws Exception {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson"); /*URL*/
        urlBuilder.append("?" + "serviceKey=" + URL_ENCODED_SERVICE_KEY);
        urlBuilder.append("&" + "pageNo=" + 1);
        urlBuilder.append("&" + "numOfRows=" + 10);
        urlBuilder.append("&" + "startCreateDt=" + DateTimeUtil.getDateString());
        urlBuilder.append("&" + "endCreateDt=" + DateTimeUtil.getDateString());

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
        System.out.println(sb.toString());
    }

    @Override
    public String getRegion() {
        return null;
    }

    @Override
    public Integer getNewLocalCaseCount() {
        return null;
    }

    @Override
    public Integer getNewForeignCaseCount() {
        return null;
    }
}
