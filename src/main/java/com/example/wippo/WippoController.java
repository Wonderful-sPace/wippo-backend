package com.example.wippo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WippoController {

    @RequestMapping("/test")
    public String test() {
        return "wippo test";
    }
}
