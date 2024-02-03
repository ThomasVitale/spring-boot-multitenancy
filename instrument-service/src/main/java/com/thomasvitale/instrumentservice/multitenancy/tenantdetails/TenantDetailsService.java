package com.thomasvitale.instrumentservice.multitenancy.tenantdetails;

import java.util.List;

import org.springframework.lang.Nullable;

/**
 * Core interface which loads tenant-specific data.
 * It is used throughout the framework as a tenant DAO.
 */
public interface TenantDetailsService {

    List<TenantDetails> loadAllTenants();

    @Nullable
    TenantDetails loadTenantByIdentifier(String identifier);

}
