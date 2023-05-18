package com.thomasvitale.instrumentservice;

import org.springframework.boot.SpringApplication;

public class TestInstrumentServiceApplication {
  
	public static void main(String[] args) {
		SpringApplication.from(InstrumentServiceApplication::main)
	  		.with(TestcontainersConfig.class)
	  		.run(args);
	}

}
