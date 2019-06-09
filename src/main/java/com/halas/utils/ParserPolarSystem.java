package com.halas.utils;

public class ParserPolarSystem {
    private ParserPolarSystem() {
    }

    public static double getCartesianX(double polarRadius, double polarAngle) {
        return polarRadius * Math.cos(polarAngle);
    }

    public static double getCartesianY(double polarRadius, double polarAngle) {
        return polarRadius * Math.sin(polarAngle);
    }
}
