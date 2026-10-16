package com.thomasvitale.instrumentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InstrumentServiceApplication {

	static void main(String[] args) {
		SpringApplication.run(InstrumentServiceApplication.class, args);
	}

}
