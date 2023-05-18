package com.thomasvitale.instrumentservice.multitenancy.data.cache;

import java.lang.reflect.Method;

import com.thomasvitale.instrumentservice.multitenancy.context.TenantContext;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class TenantKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		return SimpleKeyGenerator.generateKey(TenantContext.getTenantId(), params);
	}
  
}
