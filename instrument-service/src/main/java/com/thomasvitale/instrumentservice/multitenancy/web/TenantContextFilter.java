package com.thomasvitale.instrumentservice.multitenancy.web;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.thomasvitale.instrumentservice.multitenancy.context.TenantContextHolder;
import com.thomasvitale.instrumentservice.multitenancy.context.resolvers.HttpHeaderTenantResolver;
import com.thomasvitale.instrumentservice.multitenancy.exceptions.TenantNotFoundException;
import com.thomasvitale.instrumentservice.multitenancy.exceptions.TenantResolutionException;
import com.thomasvitale.instrumentservice.multitenancy.tenantdetails.TenantDetailsService;

import io.micrometer.common.KeyValue;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.ServerHttpObservationFilter;

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

        if (StringUtils.hasText(tenantIdentifier) && isTenantValid(tenantIdentifier)) {
            TenantContextHolder.setTenantIdentifier(tenantIdentifier);
        } else {
            throw new TenantResolutionException("A valid tenant must be specified for requests to %s".formatted(request.getRequestURI()));
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
	}

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/actuator");
    }

    private boolean isTenantValid(String tenantIdentifier) {
        var tenantDetails = tenantDetailsService.loadTenantByIdentifier(tenantIdentifier);
        return tenantDetails != null && tenantDetails.enabled();
    }

}
