package com.spring.sendaudio_client.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class UserHeader {
    private String tid;
    private String vin;
    private String vrCodec;

    public UserHeader(String tid, String vin, String vrCodec) {
        this.tid = tid;
        this.vin = vin;
        this.vrCodec = vrCodec;
    }
}
