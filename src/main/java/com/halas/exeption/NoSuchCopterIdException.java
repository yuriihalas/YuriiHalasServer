package com.halas.exeption;

public class NoSuchCopterIdException extends Exception {

    public NoSuchCopterIdException() {
        super();
    }

    public NoSuchCopterIdException(String mess) {
        super(mess);
    }
}
