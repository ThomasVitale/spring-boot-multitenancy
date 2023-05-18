package com.thomasvitale.edgeservice.multitenancy;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tenant.security")
public record TenantSecurityProperties(
		URI issuerBaseUri,
		String clientId,
		String clientSecret
){}
