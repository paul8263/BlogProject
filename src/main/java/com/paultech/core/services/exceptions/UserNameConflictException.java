package com.paultech.core.services.exceptions;

/**
 * Created by paulzhang on 18/03/15.
 */
public class UserNameConflictException extends RuntimeException {
    public UserNameConflictException(String message, Throwable cause) {
        super(message,cause);
    }

    public UserNameConflictException(String message) {
        super(message);
    }

    public UserNameConflictException() {
        super();
    }
}
