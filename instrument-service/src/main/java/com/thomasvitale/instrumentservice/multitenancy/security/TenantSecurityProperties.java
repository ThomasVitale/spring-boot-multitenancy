package com.thomasvitale.instrumentservice.multitenancy.security;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "multitenancy.security")
public record TenantSecurityProperties(
		URI issuerBaseUri
){}
