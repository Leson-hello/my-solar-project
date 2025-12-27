package com.s.solar_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SolarBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolarBackendApplication.class, args);
	}

}
