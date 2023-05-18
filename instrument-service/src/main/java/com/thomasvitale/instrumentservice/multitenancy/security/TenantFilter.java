package com.thomasvitale.instrumentservice.multitenancy.security;

import java.io.IOException;
import java.util.Optional;

import com.thomasvitale.instrumentservice.multitenancy.context.TenantContext;
import com.thomasvitale.instrumentservice.multitenancy.resolver.HttpHeaderTenantResolver;
import io.micrometer.common.KeyValue;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.ServerHttpObservationFilter;

@Component
public class TenantFilter extends OncePerRequestFilter {

	private final HttpHeaderTenantResolver httpRequestTenantResolver;

	public TenantFilter(HttpHeaderTenantResolver httpHeaderTenantResolver) {
		this.httpRequestTenantResolver = httpHeaderTenantResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Optional.ofNullable(httpRequestTenantResolver.resolveTenantId(request)).ifPresent(tenantId -> {
			TenantContext.setTenantId(tenantId);
			configureLogs(tenantId);
			configureTraces(tenantId, request);
		});

		filterChain.doFilter(request, response);

		clear();
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
		TenantContext.clear();
	}

}
