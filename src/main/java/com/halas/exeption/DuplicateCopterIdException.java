package com.halas.exeption;

public class DuplicateCopterIdException extends Exception {

    public DuplicateCopterIdException() {
        super();
    }

    public DuplicateCopterIdException(String mess) {
        super(mess);
    }
}
