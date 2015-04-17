package com.paultech.core.services.exceptions;

/**
 * Created by paulzhang on 18/03/15.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message, Throwable cause) {
        super(message,cause);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException() {
        super();
    }
}
