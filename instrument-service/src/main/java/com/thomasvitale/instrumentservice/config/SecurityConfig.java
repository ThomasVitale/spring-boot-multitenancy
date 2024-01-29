package com.thomasvitale.instrumentservice.config;

import jakarta.servlet.http.HttpServletRequest;

import com.thomasvitale.instrumentservice.multitenancy.web.TenantContextFilter;

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
			TenantContextFilter tenantContextFilter
	) throws Exception {
		return http
			.authorizeHttpRequests(request -> request
				.requestMatchers("/actuator/**").permitAll()
				.anyRequest().authenticated())
			.oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver))
			.addFilterBefore(tenantContextFilter, BearerTokenAuthenticationFilter.class)
			.build();
	}

}
