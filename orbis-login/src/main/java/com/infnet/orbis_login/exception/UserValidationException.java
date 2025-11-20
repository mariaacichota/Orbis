package com.infnet.orbis_login.exception;

public class UserValidationException extends RuntimeException {
    private final String message;

    public UserValidationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}