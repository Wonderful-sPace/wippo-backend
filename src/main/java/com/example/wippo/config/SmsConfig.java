package com.example.wippo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.wippo.domain.auth.phone.PhoneAuthService;

@Configuration
public class SmsConfig {

    @Bean
    @Profile({"local", "dev", "test"})
    public PhoneAuthService.SmsSender smsSender() {
        return (to, msg) -> System.out.println("[SMS STUB] to=" + to + ", msg=" + msg);
    }
}
