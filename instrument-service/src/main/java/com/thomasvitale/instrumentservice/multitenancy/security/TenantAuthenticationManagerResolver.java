package com.thomasvitale.instrumentservice.multitenancy.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.HttpServletRequest;

import com.thomasvitale.instrumentservice.multitenancy.context.TenantContextHolder;
import com.thomasvitale.instrumentservice.multitenancy.tenantdetails.TenantDetailsService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
public class TenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

	private static final Map<String,AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();
	private final TenantDetailsService tenantDetailsService;

	public TenantAuthenticationManagerResolver(TenantDetailsService tenantDetailsService) {
        this.tenantDetailsService = tenantDetailsService;
	}

	@Override
	public AuthenticationManager resolve(HttpServletRequest request) {
		var tenantId = TenantContextHolder.getRequiredTenantIdentifier();
		return authenticationManagers.computeIfAbsent(tenantId, this::buildAuthenticationManager);
	}

	private AuthenticationManager buildAuthenticationManager(String tenantId) {
		var issuerUri = tenantDetailsService.loadTenantByIdentifier(tenantId).issuer();
		var jwtAuthenticationprovider = new JwtAuthenticationProvider(JwtDecoders.fromIssuerLocation(issuerUri));
		return jwtAuthenticationprovider::authenticate;
	}

}
