package com.thomasvitale.edgeservice.multitenancy.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.thomasvitale.edgeservice.multitenancy.exceptions.TenantResolutionException;
import com.thomasvitale.edgeservice.multitenancy.tenantdetails.TenantDetailsService;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class TenantClientRegistrationRepository implements ReactiveClientRegistrationRepository {

	private static final Map<String,Mono<ClientRegistration>> clientRegistrations = new ConcurrentHashMap<>();

    private final TenantDetailsService tenantDetailsService;

	public TenantClientRegistrationRepository(TenantDetailsService tenantDetailsService) {
        this.tenantDetailsService = tenantDetailsService;
	}

	@Override
	public Mono<ClientRegistration> findByRegistrationId(String registrationId) {
		return clientRegistrations.computeIfAbsent(registrationId, this::buildClientRegistration);
	}

	private Mono<ClientRegistration> buildClientRegistration(String registrationId) {
        var tenantDetails = tenantDetailsService.loadTenantByIdentifier(registrationId);
        if (tenantDetails == null) {
            throw new TenantResolutionException("A valid tenant must be specified for authentication requests");
        }
		return Mono.just(ClientRegistrations.fromOidcIssuerLocation(tenantDetails.issuer())
			.registrationId(registrationId)
			.clientId(tenantDetails.clientId())
			.clientSecret(tenantDetails.clientSecret())
			.redirectUri("{baseUrl}/login/oauth2/code/" + registrationId)
			.scope("openid")
			.build());
	}

}
