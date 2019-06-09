package com.halas.exeption;

public class MaximumDistanceExceededException extends Exception{

    public MaximumDistanceExceededException() {
        super();
    }

    public MaximumDistanceExceededException(String mess) {
        super(mess);
    }
}
