package com.thomasvitale.instrumentservice.multitenancy.data.hibernate;

import java.util.Map;
import java.util.Objects;

import com.thomasvitale.instrumentservice.multitenancy.context.TenantContextHolder;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    public static final String DEFAULT_TENANT = "DEFAULT";

	@Override
	public String resolveCurrentTenantIdentifier() {
		return Objects.requireNonNullElse(TenantContextHolder.getTenantIdentifier(), DEFAULT_TENANT);
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

	@Override
	public void customize(Map<String, Object> hibernateProperties) {
		hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
	}

}
