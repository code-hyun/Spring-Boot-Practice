package com.spring.receiveaudio_server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TestController {

    @GetMapping("/test")
    public String test(@RequestBody String test){
        return test;
    }
}
