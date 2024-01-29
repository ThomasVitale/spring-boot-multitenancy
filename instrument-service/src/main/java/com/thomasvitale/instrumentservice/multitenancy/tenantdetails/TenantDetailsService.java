package com.thomasvitale.instrumentservice.multitenancy.tenantdetails;

import com.thomasvitale.instrumentservice.multitenancy.exceptions.TenantNotFoundException;

import java.util.List;

/**
 * Core interface which loads tenant-specific data.
 * It is used throughout the framework as a tenant DAO.
 */
public interface TenantDetailsService {

    List<TenantDetails> loadAllTenants();

    TenantDetails loadTenantByIdentifier(String identifier) throws TenantNotFoundException;

}
