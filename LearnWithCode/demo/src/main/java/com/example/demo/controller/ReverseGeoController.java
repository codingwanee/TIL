package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ReverseGeoController {



    // naver geoservice api client id : qdodimuqqf  (X-NCP-APIGW-API-KEY-ID)
    // naver geoservice api key : C79nPEhuWcv0ORptDVVSFBGZaKxZ1GbyffzwIX5f (X-NCP-APIGW-API-KEY)

    @GetMapping
    public HttpEntity<?> getAddressByReverseGeo() {


        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
