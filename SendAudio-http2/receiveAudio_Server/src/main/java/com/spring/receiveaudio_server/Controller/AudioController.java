package com.spring.receiveaudio_server.Controller;

import com.spring.receiveaudio_server.protocol.UserHeader;
import com.spring.receiveaudio_server.stt.RTZStt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Map;

@RestController
@Slf4j
public class AudioController {
    private final RTZStt rtzStt;
    boolean TOKEN = false;
    String Token = "";
    @Autowired
    public AudioController(RTZStt rtzStt) {
        this.rtzStt = rtzStt;
    }
    @PostMapping("receiveAudio")
    public ResponseEntity<String> receiveAudio(@RequestBody byte[] audioBuf, @RequestHeader Map<String, String> headers) throws Exception {

        UserHeader userHeader = new UserHeader(headers.get("tid"), "vin_test", headers.get("vrcodec"));
        if(TOKEN){
            TOKEN = true;
        }else{
            Token = rtzStt.getAccessToken();
            TOKEN = true;
        }
        String endMarker = "end"; // End marker for the audio stream
        String filePath = "D:\\01. project\\Spring Boot\\practice_Spring_Boot\\SendAudio-http2\\receiveAudio_Server\\src\\main\\resources\\audio\\" + headers.get("tid") + "-" + "outputFile.opus"; // File path for the output file

        log.info("received chunk size : {}", audioBuf.length);
        // ** 해결 해야할 부분 :  음성을 텍스트로 바꾼 결과
        String speachToText = rtzStt.stt(audioBuf, Token);
        log.info(speachToText);


        try (FileOutputStream fos = new FileOutputStream(filePath, true)) {
            fos.write(audioBuf);

            if (new String(audioBuf).equals(endMarker) || audioBuf.length == 3) {
                log.info("audioBuf : {}", new String(audioBuf));

                fos.close();
                return ResponseEntity.ok("end");
            } else {
                return ResponseEntity.ok("chunk received");
            }
        } catch (IOException e) {
            log.error("Error file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }



    }


}
