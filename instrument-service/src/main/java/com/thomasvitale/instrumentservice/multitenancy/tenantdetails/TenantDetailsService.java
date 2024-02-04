package com.thomasvitale.instrumentservice.multitenancy.tenantdetails;

import java.util.List;

import org.springframework.lang.Nullable;

public interface TenantDetailsService {

    List<TenantDetails> loadAllTenants();

    @Nullable
    TenantDetails loadTenantByIdentifier(String identifier);

}
