package com.paultech.rest.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by paulzhang on 6/04/15.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, Throwable cause) {
        super(message,cause);
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super();
    }
}
