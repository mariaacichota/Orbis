package br.com.orbis.Orbis.exception;

public class UserValidationException extends RuntimeException {
    private String message;

    public UserValidationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}