package com.thomasvitale.instrumentservice.multitenancy.exceptions;

/**
 * Thrown when no tenant information is found in a given context.
 *
 * @author Thomas Vitale
 */
public class TenantNotFoundException extends IllegalStateException {

    public TenantNotFoundException() {
        super("No tenant found in the current context");
    }

    public TenantNotFoundException(String message) {
        super(message);
    }

}
