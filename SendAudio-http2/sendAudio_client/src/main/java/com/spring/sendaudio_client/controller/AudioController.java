package com.spring.sendaudio_client.controller;

import com.spring.sendaudio_client.protocol.UserHeader;
import jakarta.activation.MimeType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class AudioController {
    private UserHeader userHeader;
    private HttpHeaders headers;

    @PostMapping
    public void sendAudio(byte[] audioBuf){
        log.info("size : {}", audioBuf.length);

        userHeader = new UserHeader("test","test");
        headers = new HttpHeaders();
        headers.add("tid", userHeader.getTid());
        headers.add("vin", userHeader.getVin());
        headers.add("content-type", "application/json");

        byte[] chunk = new byte[320];
        
        HttpEntity entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
//        while (chunk == 0){
//            chunk = audioBuf.
//        }
    }
}
