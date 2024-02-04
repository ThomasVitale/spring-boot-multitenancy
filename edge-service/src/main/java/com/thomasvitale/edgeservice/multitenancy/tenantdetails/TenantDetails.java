package com.thomasvitale.edgeservice.multitenancy.tenantdetails;

public record TenantDetails(
        String identifier,
        boolean enabled,
        String clientId,
        String clientSecret,
        String issuer
) {}
