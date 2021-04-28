package com.weather.weatherdataapi.util;

import com.weather.weatherdataapi.model.dto.ReverseGeocodingResponseDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReverseGeoCoding {

    public ReverseGeocodingResponseDto reverseGeocoding(String longitude, String latitude) throws ParseException {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-NCP-APIGW-API-KEY-ID", "et23d38qwi");
        headers.add("X-NCP-APIGW-API-KEY", "6RmECdY1MD4qhrXo7UNCFIzkfBr2XmheawWfrB8Y");

        String locate = longitude + "," + latitude;
        String url = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?sourcecrs=epsg:4326&orders=legalcode,admcode&output=json&coords=" + locate;
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        String data = responseEntity.getBody();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(data);
        JSONArray results = (JSONArray) jsonObject.get("results");
        JSONObject region = (JSONObject) results.get(1);
        JSONObject region2 = (JSONObject) region.get("region");
        JSONObject area1 = (JSONObject) region2.get("area1");
        String area1_name = (String) area1.get("name");
        String alias = (String) area1.get("alias");
        JSONObject area2 = (JSONObject) region2.get("area2");
        String area2_name = (String) area2.get("name");
        JSONObject area3 = (JSONObject) region2.get("area3");
        String area3_name = (String) area3.get("name");

        ReverseGeocodingResponseDto reverseGeocodingResponseDto = new ReverseGeocodingResponseDto();
        reverseGeocodingResponseDto.setBigRegion(area1_name);
        reverseGeocodingResponseDto.setSmallRegion(area2_name);
        reverseGeocodingResponseDto.setAlias(alias);

        return reverseGeocodingResponseDto;
    }

}
