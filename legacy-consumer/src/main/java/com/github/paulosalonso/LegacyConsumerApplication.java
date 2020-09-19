package com.github.paulosalonso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class LegacyConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LegacyConsumerApplication.class, args);
	}

}
