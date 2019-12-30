package com.shaun.SerialPortClient.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LightService {

    private static final Logger log = LoggerFactory.getLogger(LightService.class);

    private static final String BASE_URL = "http://iamfox.natapp1.cc/10060/api";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;

    public Object queryDeviceList() throws IOException {
        Long currentTimestamp = System.currentTimeMillis();
        // body
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("pageNo", 1);
        bodyMap.put("pageSize", 20);
        bodyMap.put("nameLike", true);
        // bodyMap.put("type","s_light");
        String jsonParam = mapper.writeValueAsString(bodyMap);

        // header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("timestamp", currentTimestamp.toString());
        requestHeaders.add("sign", "application/json");
        requestHeaders.add("client-key", "huajian");

        HttpEntity<String> request = new HttpEntity<String>(jsonParam, requestHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/device/list", request, String.class);
        String outResult = response.getBody();
        JSONObject resultJson = mapper.readValue(outResult, JSONObject.class);

        log.info("result:" + resultJson);
        return outResult;
    }

}