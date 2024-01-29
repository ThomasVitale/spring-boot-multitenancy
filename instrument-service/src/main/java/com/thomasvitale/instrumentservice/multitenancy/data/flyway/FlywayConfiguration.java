package com.thomasvitale.instrumentservice.multitenancy.data.flyway;

import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.thomasvitale.instrumentservice.multitenancy.data.hibernate.TenantIdentifierResolver.DEFAULT_SCHEMA;

@Configuration
public class FlywayConfiguration {

    private static final String DEFAULT_MIGRATION_LOCATION = "db/migration/default";

    @Bean
    FlywayConfigurationCustomizer flywayConfigurationCustomizer() {
        return configuration -> {
            configuration
                    .locations(DEFAULT_MIGRATION_LOCATION)
                    .schemas(DEFAULT_SCHEMA);
        };
    }

}
