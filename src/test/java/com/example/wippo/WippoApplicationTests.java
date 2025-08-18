package com.example.wippo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.wippo.domain.auth.phone.PhoneAuthService;
import com.example.wippo.domain.auth.social.provider.SocialClient;

@SpringBootTest
@ActiveProfiles("test")
class WippoApplicationTests {
	@MockitoBean
	PhoneAuthService.SmsSender smsSender;

	@MockitoBean
    SocialClient socialClient;
	
	@Test
	void contextLoads() {
		System.out.println("=== Test Running ===");
	}

}
