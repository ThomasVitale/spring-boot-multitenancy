package com.thomasvitale.instrumentservice.multitenancy.data.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import io.arconia.multitenancy.core.tenantdetails.TenantDetailsService;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@DependsOnDatabaseInitialization
public class ConnectionProvider implements MultiTenantConnectionProvider<String>, HibernatePropertiesCustomizer {

	private static final String DEFAULT_SCHEMA = "default";

  	private final DataSource dataSource;
    private final TenantDetailsService tenantDetailsService;

	ConnectionProvider(DataSource dataSource, TenantDetailsService tenantDetailsService) {
		this.dataSource = dataSource;
        this.tenantDetailsService = tenantDetailsService;
    }

	@Override
	public Connection getAnyConnection() throws SQLException {
		return getConnection(TenantIdentifierResolver.DEFAULT_TENANT);
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		Connection connection = dataSource.getConnection();
		connection.setSchema(resolveSchema(tenantIdentifier));
		return connection;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
    	connection.setSchema(DEFAULT_SCHEMA);
		connection.close();
  	}

	private String resolveSchema(String tenantIdentifier) {
		if (TenantIdentifierResolver.DEFAULT_TENANT.equals(tenantIdentifier)) {
			return DEFAULT_SCHEMA;
		}

		var tenantDetails = tenantDetailsService.loadTenantByIdentifier(tenantIdentifier);
		Assert.notNull(tenantDetails, "tenantDetails cannot be null for tenant: " + tenantIdentifier);

		String schema = tenantDetails.attribute("schema", String.class);
		Assert.hasText(schema, "schema attribute cannot be null or empty for tenant: " + tenantIdentifier);
		return schema;
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return true;
	}

	@Override
	public boolean isUnwrappableAs(Class<?> unwrapType) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		throw new UnsupportedOperationException("Unimplemented method 'unwrap'.");
	}

	@Override
	public void customize(Map<String, Object> hibernateProperties) {
		hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
	}

}
