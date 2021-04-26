package com.weather.weatherdataapi.util;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class ReverseGeoCoding {

    @PostConstruct
    public void reverseGeocoding() {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-NCP-APIGW-API-KEY-ID", "et23d38qwi");
        headers.add("X-NCP-APIGW-API-KEY", "6RmECdY1MD4qhrXo7UNCFIzkfBr2XmheawWfrB8Y");

        String locate = "128.12345,37.98776";
        String url = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?sourcecrs=epsg:4326&orders=legalcode,admcode&output=json&coords=" + locate;
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        System.out.println(responseEntity);

    }

}
