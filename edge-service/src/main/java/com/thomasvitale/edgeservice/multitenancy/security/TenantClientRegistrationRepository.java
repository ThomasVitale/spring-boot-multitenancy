package com.thomasvitale.edgeservice.multitenancy.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.arconia.multitenancy.core.exceptions.TenantVerificationException;
import io.arconia.multitenancy.core.tenantdetails.TenantDetailsService;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class TenantClientRegistrationRepository implements ClientRegistrationRepository {

	private static final Map<String,ClientRegistration> clientRegistrations = new ConcurrentHashMap<>();

    private final TenantDetailsService tenantDetailsService;

	public TenantClientRegistrationRepository(TenantDetailsService tenantDetailsService) {
        this.tenantDetailsService = tenantDetailsService;
	}

	@Override
	public ClientRegistration findByRegistrationId(String registrationId) {
		return clientRegistrations.computeIfAbsent(registrationId, this::buildClientRegistration);
	}

	private ClientRegistration buildClientRegistration(String registrationId) {
        var tenantDetails = tenantDetailsService.loadTenantByIdentifier(registrationId);
        if (tenantDetails == null) {
            throw new TenantVerificationException("A valid tenant must be specified for authentication requests");
        }

        String issuerUri = tenantDetails.attribute("issuer-uri", String.class);
        String clientId = tenantDetails.attribute("client-id", String.class);
        String clientSecret = tenantDetails.attribute("client-secret", String.class);

        Assert.hasText(issuerUri, "issuerUri cannot be null or empty");
        Assert.hasText(clientId, "clientId cannot be null or empty");
        Assert.hasText(clientSecret, "clientSecret cannot be null or empty");

		return ClientRegistrations.fromOidcIssuerLocation(issuerUri)
			.registrationId(registrationId)
			.clientId(clientId)
			.clientSecret(clientSecret)
			.redirectUri("{baseUrl}/login/oauth2/code/" + registrationId)
			.scope("openid")
			.build();
	}

}
