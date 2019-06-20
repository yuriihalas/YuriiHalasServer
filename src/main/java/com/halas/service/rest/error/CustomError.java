package com.halas.service.rest.error;

public class CustomError {
    private String message;

    public CustomError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CustomError{" +
                "message='" + message + '\'' +
                '}';
    }
}
