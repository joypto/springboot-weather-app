package com.weather.weatherdataapi.util.openapi.geo.naver;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ReverseGeoCodingApi {

    public ReverseGeocodingResponseDto reverseGeocoding(CoordinateDto coordinateDto) throws ParseException {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-NCP-APIGW-API-KEY-ID", "et23d38qwi");
        headers.add("X-NCP-APIGW-API-KEY", "6RmECdY1MD4qhrXo7UNCFIzkfBr2XmheawWfrB8Y");

        String locate = coordinateDto.getLongitude() + "," + coordinateDto.getLatitude();
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
        if (area2_name.equals("")){
            area2_name = area1_name;
        }
        
        ReverseGeocodingResponseDto reverseGeocodingResponseDto = new ReverseGeocodingResponseDto();
        reverseGeocodingResponseDto.setBigRegion(area1_name);
        reverseGeocodingResponseDto.setSmallRegion(area2_name);
        reverseGeocodingResponseDto.setAlias(alias);
        System.out.println(area2_name);
        return reverseGeocodingResponseDto;
    }

}
