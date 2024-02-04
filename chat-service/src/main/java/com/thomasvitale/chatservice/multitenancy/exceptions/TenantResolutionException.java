package com.thomasvitale.chatservice.multitenancy.exceptions;

public class TenantResolutionException extends IllegalStateException {

    public TenantResolutionException() {
        super("Error when trying to resolve the current tenant");
    }

    public TenantResolutionException(String message) {
        super(message);
    }

}
