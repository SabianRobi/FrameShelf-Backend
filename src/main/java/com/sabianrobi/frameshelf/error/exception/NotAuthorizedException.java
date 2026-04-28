package com.sabianrobi.frameshelf.error.exception;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(final String message) {
        super(message);
    }
}
