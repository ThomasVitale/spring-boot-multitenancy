package com.thomasvitale.instrumentservice.multitenancy.tenantdetails;

public record TenantDetails(
        String identifier,
        boolean enabled
) {}
