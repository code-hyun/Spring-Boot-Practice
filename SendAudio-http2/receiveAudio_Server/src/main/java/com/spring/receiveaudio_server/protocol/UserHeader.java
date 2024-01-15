package com.spring.receiveaudio_server.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
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
