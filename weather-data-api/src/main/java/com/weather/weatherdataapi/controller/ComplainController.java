package com.weather.weatherdataapi.controller;

import com.weather.weatherdataapi.Global;
import com.weather.weatherdataapi.model.dto.requestdto.ComplainReqeustDto;
import com.weather.weatherdataapi.service.ComplainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ComplainController {

    private final ComplainService complainService;

    @PostMapping("/api/complains")
    public ResponseEntity<String> postComplain(@RequestBody ComplainReqeustDto complainReqeustDto, @RequestHeader(value = Global.IDENTIFICATION_TEXT, required = false) String identification) {
        complainService.postComplain(complainReqeustDto, identification);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(Global.IDENTIFICATION_TEXT, identification);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("Success");
    }

}
