package com.thomasvitale.instrumentservice.multitenancy.tenantdetails;

import java.util.List;

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
                .filter(TenantDetails::enabled)
                .filter(tenantDetails -> identifier.equals(tenantDetails.identifier()))
                .findFirst().orElse(null);
    }

}
