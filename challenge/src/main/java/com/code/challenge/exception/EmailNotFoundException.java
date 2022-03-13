package com.code.challenge.exception;

public class EmailNotFoundException  extends RuntimeException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
