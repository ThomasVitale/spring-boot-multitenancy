package com.thomasvitale.edgeservice.multitenancy.gateway;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.arconia.multitenancy.core.context.TenantContext;
import io.arconia.multitenancy.core.exceptions.TenantNotFoundException;
import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TenantFilterFunctionsTests {

	@Test
	void propagatesCurrentTenantAsDefaultHeader() throws Exception {
		ServerRequest downstream = runWithTenant("dukes", TenantFilterFunctions.propagateTenant(), request());

		assertThat(downstream.headers().header("X-TenantId")).containsExactly("dukes");
	}

	@Test
	void propagatesCurrentTenantAsCustomHeader() throws Exception {
		ServerRequest downstream = runWithTenant("beans", TenantFilterFunctions.propagateTenant("X-Tenant"),
				request());

		assertThat(downstream.headers().header("X-Tenant")).containsExactly("beans");
		assertThat(downstream.headers().header("X-TenantId")).isEmpty();
	}

	@Test
	void replacesClientSuppliedTenantHeader() throws Exception {
		MockHttpServletRequest servletRequest = request();
		servletRequest.addHeader("X-TenantId", "spoofed");

		ServerRequest downstream = runWithTenant("dukes", TenantFilterFunctions.propagateTenant(), servletRequest);

		assertThat(downstream.headers().header("X-TenantId")).containsExactly("dukes");
	}

	@Test
	void failsWhenNoTenantIsBound() {
		ServerRequest request = ServerRequest.create(request(), List.of());

		assertThatThrownBy(() -> TenantFilterFunctions.propagateTenant().filter(request, r -> ServerResponse.ok().build()))
			.isInstanceOf(TenantNotFoundException.class);
	}

	private static MockHttpServletRequest request() {
		return new MockHttpServletRequest("GET", "/instruments");
	}

	private static ServerRequest runWithTenant(String tenantIdentifier,
			HandlerFilterFunction<ServerResponse, ServerResponse> filter, MockHttpServletRequest servletRequest)
			throws Exception {
		ServerRequest request = ServerRequest.create(servletRequest, List.of());
		AtomicReference<ServerRequest> downstream = new AtomicReference<>();
		TenantContext.where(tenantIdentifier).call(() -> filter.filter(request, r -> {
			downstream.set(r);
			return ServerResponse.ok().build();
		}));
		return downstream.get();
	}

}
