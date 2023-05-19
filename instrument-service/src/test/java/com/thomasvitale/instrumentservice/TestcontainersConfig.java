package com.thomasvitale.instrumentservice;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {
  
	@Bean
	@RestartScope
	@ServiceConnection
	PostgreSQLContainer<?> postgreSQLContainer() {
		return new PostgreSQLContainer<>("postgres:15");
	}

	@Bean
	@ServiceConnection("redis")
	GenericContainer<?> redisContainer() {
	return new GenericContainer<>("redis:7")
    	.withExposedPorts(6379);
	}

}
