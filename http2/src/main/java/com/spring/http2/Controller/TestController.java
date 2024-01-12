package com.spring.http2.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class TestController {

    @GetMapping("hello")
    public String hello(){
        return "hello";
    }


}
