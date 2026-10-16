package com.thomasvitale.edgeservice.multitenancy.gateway;

import io.arconia.multitenancy.core.context.TenantContext;

import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * Gateway filter functions to propagate the current tenant to downstream services.
 * <p>
 * This filter reads the tenant identifier from the {@link TenantContext}
 * and forwards it to the downstream service as an HTTP header,
 * replacing any value supplied by the client.
 * <p>
 * Registered through {@link FilterSupplier} so that it can be referenced from
 * configuration properties as {@code PropagateTenant=} (default header) or
 * {@code PropagateTenant=X-Custom-Header}.
 */
public final class TenantFilterFunctions {

	static final String DEFAULT_TENANT_HEADER_NAME = "X-TenantId";

	private TenantFilterFunctions() {}

	@Shortcut
	public static HandlerFilterFunction<ServerResponse, ServerResponse> propagateTenant() {
		return propagateTenant(DEFAULT_TENANT_HEADER_NAME);
	}

	@Shortcut
	public static HandlerFilterFunction<ServerResponse, ServerResponse> propagateTenant(String headerName) {
		return (request, next) -> {
			String tenantIdentifier = TenantContext.getRequiredTenantIdentifier();
			ServerRequest modified = ServerRequest.from(request)
				.headers(headers -> headers.set(headerName, tenantIdentifier))
				.build();
			return next.handle(modified);
		};
	}

	/**
	 * Makes the filter functions in this class discoverable by the gateway.
	 */
	public static class FilterSupplier extends SimpleFilterSupplier {

		public FilterSupplier() {
			super(TenantFilterFunctions.class);
		}

	}

}
