package com.thomasvitale.chatservice.multitenancy.tenantdetails;

public record TenantDetails(
        String identifier,
        boolean enabled
) {}
