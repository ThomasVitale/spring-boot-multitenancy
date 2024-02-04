package com.thomasvitale.instrumentservice.multitenancy.data.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import com.thomasvitale.instrumentservice.multitenancy.tenantdetails.TenantDetailsService;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ConnectionProvider implements MultiTenantConnectionProvider<String>, HibernatePropertiesCustomizer {

  	private final DataSource dataSource;
    private final TenantDetailsService tenantDetailsService;

	ConnectionProvider(DataSource dataSource, TenantDetailsService tenantDetailsService) {
		this.dataSource = dataSource;
        this.tenantDetailsService = tenantDetailsService;
    }

	@Override
	public Connection getAnyConnection() throws SQLException {
		return getConnection("DEFAULT");
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
        var tenantDetails = tenantDetailsService.loadTenantByIdentifier(tenantIdentifier);
        var schema = tenantDetails != null ? tenantDetails.schema() : tenantIdentifier;

		Connection connection = dataSource.getConnection();
		connection.setSchema(schema);
		return connection;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
    	connection.setSchema("DEFAULT");
		connection.close();
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
