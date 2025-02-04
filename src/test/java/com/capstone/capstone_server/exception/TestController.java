package com.capstone.capstone_server.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String testException() {
        // 의도적으로 예외 발생
        throw new IllegalArgumentException("Invalid parameter provided");
    }
}
