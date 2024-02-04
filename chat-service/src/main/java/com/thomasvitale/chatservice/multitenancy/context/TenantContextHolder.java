package com.thomasvitale.chatservice.multitenancy.context;

import com.thomasvitale.chatservice.multitenancy.exceptions.TenantNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public final class TenantContextHolder {

    private static final Logger log = LoggerFactory.getLogger(TenantContextHolder.class);

    private static final ThreadLocal<String> tenantIdentifier = new ThreadLocal<>();

    private TenantContextHolder() {
    }

    public static void setTenantIdentifier(String tenant) {
        Assert.hasText(tenant, "tenant cannot be empty");
        log.trace("Setting current tenant to: {}", tenant);
        tenantIdentifier.set(tenant);
    }

    @Nullable
    public static String getTenantIdentifier() {
        return tenantIdentifier.get();
    }

    public static String getRequiredTenantIdentifier() {
        var tenant = getTenantIdentifier();
        if (!StringUtils.hasText(tenant)) {
            throw new TenantNotFoundException("No tenant found in the current context");
        }
        return tenant;
    }

    public static void clear() {
        log.trace("Clearing current tenant");
        tenantIdentifier.remove();
    }

}
