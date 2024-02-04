package com.thomasvitale.instrumentservice.multitenancy.context.resolvers;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class HttpHeaderTenantResolver implements TenantResolver<HttpServletRequest> {

    private static final String TENANT_HEADER = "X-TenantId";

	@Override
    @Nullable
	public String resolveTenantIdentifier(HttpServletRequest request) {
		return request.getHeader(TENANT_HEADER);
	}

}
