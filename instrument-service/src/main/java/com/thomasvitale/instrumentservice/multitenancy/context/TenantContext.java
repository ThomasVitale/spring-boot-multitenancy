package com.thomasvitale.instrumentservice.multitenancy.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TenantContext {

	private static final Logger log = LoggerFactory.getLogger(TenantContext.class);
	private static final ThreadLocal<String> tenantId = new InheritableThreadLocal<>();
  
	public static void setTenantId(String tenant) {
    	log.debug("Setting current tenant to {}", tenant);
    	tenantId.set(tenant);
	}

	public static String getTenantId() {
    	return tenantId.get();
  	}

	public static void clear() {
    	tenantId.remove();
  	}

}
