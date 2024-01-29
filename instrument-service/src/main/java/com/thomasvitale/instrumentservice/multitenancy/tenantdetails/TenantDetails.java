package com.thomasvitale.instrumentservice.multitenancy.tenantdetails;

/**
 * Provides core tenant information.
 */
public record TenantDetails(String identifier, String schema, boolean enabled) {}
