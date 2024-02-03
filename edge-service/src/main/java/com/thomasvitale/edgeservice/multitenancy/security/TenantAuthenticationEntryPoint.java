package com.thomasvitale.edgeservice.multitenancy.security;

import java.net.URI;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class TenantAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    private ServerRequestCache requestCache = new WebSessionServerRequestCache();

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        var baseLoginUri = "/oauth2/authorization/";
        var tenantId = exchange.getRequest().getURI().getHost().split("\\.")[0];
        var tenantLoginLocation = URI.create(baseLoginUri + tenantId);
        return this.requestCache.saveRequest(exchange)
                .then(this.redirectStrategy.sendRedirect(exchange, tenantLoginLocation));
    }

    public void setRequestCache(ServerRequestCache requestCache) {
        Assert.notNull(requestCache, "requestCache cannot be null");
        this.requestCache = requestCache;
    }

    public void setRedirectStrategy(ServerRedirectStrategy redirectStrategy) {
        Assert.notNull(redirectStrategy, "redirectStrategy cannot be null");
        this.redirectStrategy = redirectStrategy;
    }

}
