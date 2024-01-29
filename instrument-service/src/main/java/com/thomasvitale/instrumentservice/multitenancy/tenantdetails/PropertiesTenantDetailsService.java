package com.thomasvitale.instrumentservice.multitenancy.tenantdetails;

import java.util.List;

import com.thomasvitale.instrumentservice.multitenancy.exceptions.TenantNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class PropertiesTenantDetailsService implements TenantDetailsService {

    private final TenantDetailsProperties tenantDetailsProperties;

    public PropertiesTenantDetailsService(TenantDetailsProperties tenantDetailsProperties) {
        this.tenantDetailsProperties = tenantDetailsProperties;
    }

    @Override
    public List<TenantDetails> loadAllTenants() {
        return tenantDetailsProperties.tenants();
    }

    @Override
    public TenantDetails loadTenantByIdentifier(String identifier) {
        return tenantDetailsProperties.tenants().stream()
                .findFirst().orElseThrow(() -> new TenantNotFoundException("tenant doesn't exist"));
    }

}
