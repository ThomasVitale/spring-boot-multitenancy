package com.thomasvitale.instrumentservice.multitenancy.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This configuration is disabled because when Spring Security
 * is used, the tenant interception is done in the TenantFilter
 * as part of the security filter chain rather than in the
 * Spring MVC handler logic.
 */
//@Configuration
public class WebConfig implements WebMvcConfigurer  {

	private final TenantInterceptor tenantInterceptor;

	WebConfig(TenantInterceptor tenantInterceptor) {
		this.tenantInterceptor = tenantInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tenantInterceptor);
	}
  
}
