package com.thomasvitale.edgeservice.multitenancy.gateway;

import io.micrometer.common.KeyValue;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.reactive.ServerHttpObservationFilter;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;

/**
 * Custom filter to extend the AddRequestHeader built-in filter so to
 * also include tenant information into the ObservabilityContext.
 */
@Component
public class TenantGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

	@Override
	public GatewayFilter apply(NameValueConfig config) {
		return new GatewayFilter() {
			@Override
			public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
				String tenantId = ServerWebExchangeUtils.expand(exchange, config.getValue());
				ServerHttpRequest request = addTenantToRequest(exchange, tenantId, config);
				addTenantToObservation(tenantId, exchange);
				return chain.filter(exchange.mutate().request(request).build());
			}

			@Override
			public String toString() {
				return filterToStringCreator(TenantGatewayFilterFactory.this)
						.append(config.getName(), config.getValue()).toString();
			}
		};
	}

	private ServerHttpRequest addTenantToRequest(ServerWebExchange exchange, String tenantId, NameValueConfig config) {
		var tenantHeader = config.getName();
		return exchange.getRequest().mutate()
			.headers(httpHeaders -> httpHeaders.add(tenantHeader, tenantId))
			.build();
	}

	private void addTenantToObservation(String tenantId, ServerWebExchange exchange) {
		ServerHttpObservationFilter.findObservationContext(exchange).ifPresent(context ->
			context.addHighCardinalityKeyValue(KeyValue.of("tenant.id", tenantId)));
	}

}
