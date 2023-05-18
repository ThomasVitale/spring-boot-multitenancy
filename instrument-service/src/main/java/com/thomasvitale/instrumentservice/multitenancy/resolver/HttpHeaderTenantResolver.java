package com.thomasvitale.instrumentservice.multitenancy.resolver;

import com.thomasvitale.instrumentservice.multitenancy.TenantHttpProperties;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class HttpHeaderTenantResolver implements TenantResolver<HttpServletRequest> {

	private final TenantHttpProperties tenantHttpProperties;

	public HttpHeaderTenantResolver(TenantHttpProperties tenantHttpProperties) {
		this.tenantHttpProperties = tenantHttpProperties;
	}

	@Override
	public String resolveTenantId(@NonNull HttpServletRequest request) {
		return request.getHeader(tenantHttpProperties.headerName());
	}

}
