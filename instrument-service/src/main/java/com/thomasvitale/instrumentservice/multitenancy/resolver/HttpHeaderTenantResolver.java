package com.thomasvitale.instrumentservice.multitenancy.resolver;

import jakarta.servlet.http.HttpServletRequest;

import com.thomasvitale.instrumentservice.multitenancy.TenantHttpProperties;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class HttpHeaderTenantResolver implements TenantResolver<HttpServletRequest> {

	private final TenantHttpProperties tenantHttpProperties;

	public HttpHeaderTenantResolver(TenantHttpProperties tenantHttpProperties) {
		this.tenantHttpProperties = tenantHttpProperties;
	}

	@Override
    @Nullable
	public String resolveTenantIdentifier(@NonNull HttpServletRequest request) {
		return request.getHeader(tenantHttpProperties.headerName());
	}

}
