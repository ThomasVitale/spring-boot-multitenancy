package com.thomasvitale.instrumentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestInstrumentServiceApplication {

    public static void main(String[] args) {
		SpringApplication.from(InstrumentServiceApplication::main)
	  		.with(TestInstrumentServiceApplication.class)
	  		.run(args);
	}

}
