package com.code.challenge.exception;

public class PhoneNumberNotFoundException extends RuntimeException{
    public PhoneNumberNotFoundException(String message) {
        super(message);
    }
}
