package com.paultech.rest.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by paulzhang on 4/04/15.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    public ConflictException(String message, Throwable cause) {
        super(message,cause);
    }

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException() {
        super();
    }
}
