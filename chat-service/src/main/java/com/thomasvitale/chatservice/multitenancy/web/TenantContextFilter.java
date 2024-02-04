package com.thomasvitale.chatservice.multitenancy.web;

import com.thomasvitale.chatservice.multitenancy.context.TenantContextHolder;
import com.thomasvitale.chatservice.multitenancy.context.resolvers.HttpHeaderTenantResolver;

import com.thomasvitale.chatservice.multitenancy.exceptions.TenantNotFoundException;
import com.thomasvitale.chatservice.multitenancy.exceptions.TenantResolutionException;
import com.thomasvitale.chatservice.multitenancy.tenantdetails.TenantDetailsService;

import io.micrometer.common.KeyValue;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.ServerHttpObservationFilter;

import java.io.IOException;

@Component
public class TenantContextFilter extends OncePerRequestFilter {

	private final HttpHeaderTenantResolver httpRequestTenantResolver;
    private final TenantDetailsService tenantDetailsService;

	public TenantContextFilter(HttpHeaderTenantResolver httpHeaderTenantResolver, TenantDetailsService tenantDetailsService) {
		this.httpRequestTenantResolver = httpHeaderTenantResolver;
        this.tenantDetailsService = tenantDetailsService;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tenantIdentifier = httpRequestTenantResolver.resolveTenantIdentifier(request);

        if (StringUtils.hasText(tenantIdentifier)) {
            if (!isTenantValid(tenantIdentifier)) {
                throw new TenantNotFoundException();
            }
            TenantContextHolder.setTenantIdentifier(tenantIdentifier);
            configureLogs(tenantIdentifier);
            configureTraces(tenantIdentifier, request);
        } else {
            throw new TenantResolutionException("A tenant must be specified for requests to %s".formatted(request.getRequestURI()));
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            clear();
        }
	}

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/actuator");
    }

    private boolean isTenantValid(String tenantIdentifier) {
        var tenantDetails = tenantDetailsService.loadTenantByIdentifier(tenantIdentifier);
        return tenantDetails.enabled();
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
