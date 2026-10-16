package com.thomasvitale.edgeservice.multitenancy.security;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.arconia.multitenancy.core.context.TenantContext;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

/**
 * Redirects unauthenticated requests to the OAuth2 login flow
 * of the tenant in the current context.
 */
@Component
public class TenantAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String BASE_LOGIN_URI = "/oauth2/authorization/";

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        var tenantId = TenantContext.getRequiredTenantIdentifier();
        var tenantLoginLocation = BASE_LOGIN_URI + tenantId;
        redirectStrategy.sendRedirect(request, response, tenantLoginLocation);
    }

}
