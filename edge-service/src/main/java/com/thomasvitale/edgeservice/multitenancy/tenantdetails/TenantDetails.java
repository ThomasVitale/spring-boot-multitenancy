package com.thomasvitale.edgeservice.multitenancy.tenantdetails;

/**
 * Provides core tenant information.
 */
public record TenantDetails(String identifier, boolean enabled, String clientId, String clientSecret, String issuer) {}
