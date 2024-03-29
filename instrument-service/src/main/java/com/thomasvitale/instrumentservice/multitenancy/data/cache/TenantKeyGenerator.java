package com.thomasvitale.instrumentservice.multitenancy.data.cache;

import java.lang.reflect.Method;

import com.thomasvitale.instrumentservice.multitenancy.context.TenantContextHolder;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link KeyGenerator} that generates cache keys combining the
 * current tenant identifier with the given method and parameters.
 */
@Component
public final class TenantKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return SimpleKeyGenerator.generateKey(TenantContextHolder.getRequiredTenantIdentifier(), params);
    }

}
