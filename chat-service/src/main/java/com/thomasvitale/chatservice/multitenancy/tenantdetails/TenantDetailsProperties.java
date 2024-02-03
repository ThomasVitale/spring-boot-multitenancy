package com.thomasvitale.chatservice.multitenancy.tenantdetails;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "multitenancy")
public record TenantDetailsProperties(List<TenantDetails> tenants) { }
