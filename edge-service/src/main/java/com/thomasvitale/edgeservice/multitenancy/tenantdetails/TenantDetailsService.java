package com.thomasvitale.edgeservice.multitenancy.tenantdetails;

import java.util.List;

/**
 * Core interface which loads tenant-specific data.
 * It is used throughout the framework as a tenant DAO.
 */
public interface TenantDetailsService {

    List<TenantDetails> loadAllTenants();

    TenantDetails loadTenantByIdentifier(String identifier);

}
