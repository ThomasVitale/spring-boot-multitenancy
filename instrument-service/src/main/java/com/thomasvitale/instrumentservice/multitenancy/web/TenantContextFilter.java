package com.thomasvitale.instrumentservice.multitenancy.web;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.thomasvitale.instrumentservice.multitenancy.context.TenantContextHolder;
import com.thomasvitale.instrumentservice.multitenancy.resolver.HttpHeaderTenantResolver;

import io.micrometer.common.KeyValue;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.ServerHttpObservationFilter;

/**
 * Establish a tenant context from an HTTP request, if tenant information is available.
 */
@Component
public class TenantContextFilter extends OncePerRequestFilter {

	private final HttpHeaderTenantResolver httpRequestTenantResolver;

	public TenantContextFilter(HttpHeaderTenantResolver httpHeaderTenantResolver) {
		this.httpRequestTenantResolver = httpHeaderTenantResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tenantIdentifier = httpRequestTenantResolver.resolveTenantIdentifier(request);
        if (StringUtils.hasText(tenantIdentifier)) {
            TenantContextHolder.setTenantIdentifier(tenantIdentifier);
            configureLogs(tenantIdentifier);
            configureTraces(tenantIdentifier, request);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            clear();
        }
	}

	private void configureLogs(String tenantId) {
		MDC.put("tenantId", tenantId);
	}

	private void configureTraces(String tenantId, HttpServletRequest request) {
		ServerHttpObservationFilter.findObservationContext(request).ifPresent(context ->
				context.addHighCardinalityKeyValue(KeyValue.of("tenant.id", tenantId)));
	}

	private void clear() {
		MDC.remove("tenantId");
		TenantContextHolder.clear();
	}

}
