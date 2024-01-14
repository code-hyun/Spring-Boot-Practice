package com.spring.receiveaudio_server.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AudioController {

    @PostMapping("receiveAudio")
    public String test(@RequestBody byte[] body, HttpServletRequest request){
        String end = "finalize";
        String returnString ="end";
//        log.info(request.getHeader());
//        Collections.list(request.getHeaderNames()).forEach(v ->
//            {
//                String headerValue = request.getHeader(v);
//                log.info("headerName : " + v + " headerValue : " + headerValue);
//            }
//        );

        log.info(body.length + "");
        if(new String(body).equals(end));
        return returnString;
    }
}
