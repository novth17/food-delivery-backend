package com.lilyhien.exception;

//400 error
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}