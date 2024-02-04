package com.thomasvitale.edgeservice.multitenancy.tenantdetails;

import java.util.List;

public interface TenantDetailsService {

    List<TenantDetails> loadAllTenants();

    TenantDetails loadTenantByIdentifier(String identifier);

}
