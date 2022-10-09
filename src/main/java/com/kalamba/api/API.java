package com.kalamba.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class API {

    RestTemplate restTemplate = new RestTemplate();

    public Object callAPI(String url, Class<?> responseType) {

        // 헤더 만들기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // 헤더 오브젝트 만들기
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

        return response.getBody();
    }
}