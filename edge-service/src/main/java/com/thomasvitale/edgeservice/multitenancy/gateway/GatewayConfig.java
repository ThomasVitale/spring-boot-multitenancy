package com.thomasvitale.edgeservice.multitenancy.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class GatewayConfig {

	@Bean
	TenantFilterFunctions.FilterSupplier tenantFilterFunctionsSupplier() {
		return new TenantFilterFunctions.FilterSupplier();
	}

}
