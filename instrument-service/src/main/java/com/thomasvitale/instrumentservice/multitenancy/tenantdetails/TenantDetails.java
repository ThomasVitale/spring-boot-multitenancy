package com.thomasvitale.instrumentservice.multitenancy.tenantdetails;

/**
 * Provides core tenant information.
 */
public record TenantDetails(
        String identifier,
        boolean enabled,
        String schema,
        String issuer
) {}
