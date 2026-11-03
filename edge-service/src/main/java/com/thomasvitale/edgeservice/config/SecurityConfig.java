package com.thomasvitale.edgeservice.config;

import com.thomasvitale.edgeservice.multitenancy.security.TenantAuthenticationEntryPoint;

import io.arconia.multitenancy.web.context.TenantContextFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ClientRegistrationRepository clientRegistrationRepository,
            TenantAuthenticationEntryPoint authenticationEntryPoint,
            TenantContextFilter tenantContextFilter
    ) {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**", "/tenant-login/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(clientRegistrationRepository))
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(tenantContextFilter, OAuth2AuthorizationRequestRedirectFilter.class)
                .build();
    }

    @Bean
    FilterRegistrationBean<TenantContextFilter> tenantContextFilterRegistration(TenantContextFilter filter) {
        var registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

}
