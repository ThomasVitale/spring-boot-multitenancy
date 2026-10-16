package com.thomasvitale.instrumentservice.multitenancy.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.HttpServletRequest;

import io.arconia.multitenancy.core.context.TenantContext;
import io.arconia.multitenancy.core.tenantdetails.Tenant;
import io.arconia.multitenancy.core.tenantdetails.TenantDetailsService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class TenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

	private static final Map<String,AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();
	private final TenantDetailsService tenantDetailsService;

	public TenantAuthenticationManagerResolver(TenantDetailsService tenantDetailsService) {
        this.tenantDetailsService = tenantDetailsService;
	}

	@Override
	public AuthenticationManager resolve(HttpServletRequest request) {
		var tenantId = TenantContext.getRequiredTenantIdentifier();
		return authenticationManagers.computeIfAbsent(tenantId, this::buildAuthenticationManager);
	}

	private AuthenticationManager buildAuthenticationManager(String tenantId) {
        Tenant tenantDetails = (Tenant) tenantDetailsService.loadTenantByIdentifier(tenantId);
        Assert.notNull(tenantDetails, "tenantDetails cannot be null");

        String issuerUri = tenantDetails.attribute("issuer-uri", String.class);
        Assert.hasText(issuerUri, "issuer-uri attribute cannot be null or empty");

        var jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
		var jwtAuthenticationprovider = new JwtAuthenticationProvider(jwtDecoder);
		return jwtAuthenticationprovider::authenticate;
	}

}
