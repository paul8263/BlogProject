package com.paultech.rest.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by paulzhang on 6/04/15.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message, Throwable cause) {
        super(message,cause);
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
        super();
    }
}
