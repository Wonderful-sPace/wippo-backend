package com.example.wippo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WippoController {

    @GetMapping("/test")
    public TestResponse test() {
        // Lombok 생성자 사용
        return new TestResponse("wippo test", 1);
    }
}
