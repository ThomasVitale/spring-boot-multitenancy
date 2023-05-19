package com.thomasvitale.instrumentservice;

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {
  
	@Bean
	@RestartScope
	@ServiceConnection
	PostgreSQLContainer<?> postgreSQLContainer() {
		return new PostgreSQLContainer<>("postgres:15");
	}

}
