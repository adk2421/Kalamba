package com.kalamba.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class API {

    RestTemplate restTemplate = new RestTemplate();

    /**
     * 📢[ API 호출 ]
     * @param url
     * @param responseType
     * @return RestTemplate -> response.getBody()
     */
    public Object callAPI(String url, Class<?> responseType) {

        // 헤더 만들기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        System.out.println("API " + url);
        // 헤더 오브젝트 만들기
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<?> response = null;

        try {
            // API 호출
            response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        } catch (HttpClientErrorException e) {
            response = null;
        }
        
        return response.getBody();
    }
}