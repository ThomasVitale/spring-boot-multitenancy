package com.thomasvitale.chatservice.multitenancy.exceptions;

public class TenantNotFoundException extends IllegalStateException {

    public TenantNotFoundException() {
        super("No tenant found in the current context");
    }

    public TenantNotFoundException(String message) {
        super(message);
    }

}
