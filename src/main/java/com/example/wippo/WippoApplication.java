package com.example.wippo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WippoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WippoApplication.class, args);
	}

}
