package com.thomasvitale.instrumentservice.multitenancy.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.thomasvitale.instrumentservice.multitenancy.TenantSecurityProperties;
import com.thomasvitale.instrumentservice.multitenancy.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
public class TenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

	private static final Map<String,AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();
	private final TenantSecurityProperties tenantSecurityProperties;

	public TenantAuthenticationManagerResolver(TenantSecurityProperties tenantSecurityProperties) {
		this.tenantSecurityProperties = tenantSecurityProperties;
	}

	@Override
	public AuthenticationManager resolve(HttpServletRequest request) {
		var tenantId = TenantContextHolder.getRequiredTenantIdentifier();
		return authenticationManagers.computeIfAbsent(tenantId, this::buildAuthenticationManager);
	}

	private AuthenticationManager buildAuthenticationManager(String tenantId) {
		var issuerBaseUri = tenantSecurityProperties.issuerBaseUri().toString().strip();
		var issuerUri = issuerBaseUri + tenantId;
		var jwtAuthenticationprovider = new JwtAuthenticationProvider(JwtDecoders.fromIssuerLocation(issuerUri));
		return jwtAuthenticationprovider::authenticate;
	}

}
