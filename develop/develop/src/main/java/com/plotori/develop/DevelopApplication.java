package com.plotori.develop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevelopApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevelopApplication.class, args);
	}

}
