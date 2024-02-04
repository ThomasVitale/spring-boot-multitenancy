package com.thomasvitale.chatservice.multitenancy.context.resolvers;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface TenantResolver<T> {

    @Nullable
    String resolveTenantIdentifier(T source);

}
