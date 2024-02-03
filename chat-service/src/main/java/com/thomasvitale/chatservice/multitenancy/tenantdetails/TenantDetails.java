package com.thomasvitale.chatservice.multitenancy.tenantdetails;

/**
 * Provides core tenant information.
 */
public record TenantDetails(String identifier, boolean enabled) {}
