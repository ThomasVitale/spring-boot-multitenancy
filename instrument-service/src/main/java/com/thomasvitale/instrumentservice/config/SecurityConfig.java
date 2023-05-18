package com.thomasvitale.instrumentservice.config;

import com.thomasvitale.instrumentservice.multitenancy.security.TenantFilter;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver,
			TenantFilter tenantFilter
	) throws Exception {
		return http
			.authorizeHttpRequests(request -> request
				.requestMatchers("/actuator/**").permitAll()
				.anyRequest().authenticated())
			.oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver))
			.addFilterBefore(tenantFilter, BearerTokenAuthenticationFilter.class)
			.build();
	}

}
