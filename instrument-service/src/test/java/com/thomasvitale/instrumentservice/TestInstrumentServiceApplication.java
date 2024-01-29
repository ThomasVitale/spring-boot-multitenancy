package com.thomasvitale.instrumentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestInstrumentServiceApplication {

    @Bean
    @RestartScope
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:15.5");
    }

    public static void main(String[] args) {
		SpringApplication.from(InstrumentServiceApplication::main)
	  		.with(TestInstrumentServiceApplication.class)
	  		.run(args);
	}

}
