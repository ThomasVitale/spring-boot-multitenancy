package com.thomasvitale.instrumentservice.multitenancy.web;

import com.thomasvitale.instrumentservice.multitenancy.context.TenantContext;
import com.thomasvitale.instrumentservice.multitenancy.resolver.HttpHeaderTenantResolver;
import io.micrometer.common.KeyValue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.ServerHttpObservationFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class TenantInterceptor implements HandlerInterceptor {

	private final HttpHeaderTenantResolver httpHeaderTenantResolver;

	public TenantInterceptor(HttpHeaderTenantResolver httpHeaderTenantResolver) {
		this.httpHeaderTenantResolver = httpHeaderTenantResolver;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String tenantId = httpHeaderTenantResolver.resolveTenantId(request);
		TenantContext.setTenantId(tenantId);
		configureLogs(tenantId);
		configureTraces(tenantId, request);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		clear();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
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
