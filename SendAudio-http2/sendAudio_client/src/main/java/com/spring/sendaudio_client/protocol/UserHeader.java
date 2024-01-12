package com.spring.sendaudio_client.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
public class UserHeader {
    private String tid;
    private String vin;

    public UserHeader(String tid, String vin) {
        this.tid = tid;
        this.vin = vin;
    }
}
