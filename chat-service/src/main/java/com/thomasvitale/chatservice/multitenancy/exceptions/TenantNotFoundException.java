package com.thomasvitale.chatservice.multitenancy.exceptions;

/**
 * Thrown when no tenant information is found in a given context.
 */
public class TenantNotFoundException extends IllegalStateException {

    public TenantNotFoundException() {
        super("No tenant found in the current context");
    }

    public TenantNotFoundException(String message) {
        super(message);
    }

}
