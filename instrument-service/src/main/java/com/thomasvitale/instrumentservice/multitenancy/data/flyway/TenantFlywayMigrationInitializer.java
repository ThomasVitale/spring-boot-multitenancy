package com.thomasvitale.instrumentservice.multitenancy.data.flyway;

import javax.sql.DataSource;

import io.arconia.multitenancy.core.tenantdetails.TenantDetails;
import io.arconia.multitenancy.core.tenantdetails.TenantDetailsService;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@DependsOnDatabaseInitialization
public class TenantFlywayMigrationInitializer implements InitializingBean, Ordered {

    private static final String TENANT_MIGRATION_LOCATION = "db/migration/tenant";

    private final DataSource dataSource;
    private final Flyway defaultFlyway;
    private final TenantDetailsService tenantDetailsService;

    public TenantFlywayMigrationInitializer(DataSource dataSource, Flyway defaultFlyway, TenantDetailsService tenantDetailsService) {
        this.dataSource = dataSource;
        this.defaultFlyway = defaultFlyway;
        this.tenantDetailsService = tenantDetailsService;
    }

    @Override
    public void afterPropertiesSet() {
        tenantDetailsService.loadAllTenants().stream()
                .filter(TenantDetails::enabled)
                .forEach(tenantDetails -> {
                    String schema = tenantDetails.attribute("schema", String.class);
                    Assert.hasText(schema, "schema attribute cannot be null or empty for tenant: " + tenantDetails.identifier());

                    Flyway flyway = tenantFlyway(schema);
                    flyway.migrate();
                });
    }

    private Flyway tenantFlyway(String schema) {
        return Flyway.configure()
                .configuration(defaultFlyway.getConfiguration())
                .locations(TENANT_MIGRATION_LOCATION)
                .dataSource(dataSource)
                .schemas(schema)
                .load();
    }

    @Override
    public int getOrder() {
        // Executed after the default schema initialization in FlywayMigrationInitializer.
        return 1;
    }

}
