package com.paultech.core.services.exceptions;

/**
 * Created by paulzhang on 21/03/15.
 */
public class DuplicatedCommentException extends RuntimeException {
    public DuplicatedCommentException(String message,Throwable cause) {
        super(message,cause);
    }
    public DuplicatedCommentException(String message) {
        super(message);
    }

    public DuplicatedCommentException() {
        super();
    }
}
