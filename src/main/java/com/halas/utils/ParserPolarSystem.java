package com.halas.utils;

import java.text.DecimalFormat;

public class ParserPolarSystem {
    private ParserPolarSystem() {
    }

    public static double getCartesianX(double polarRadius, double polarAngle) {
        double polarAngleRadian = Math.toRadians(polarAngle);
        double result = polarRadius * Math.cos(polarAngleRadian);
        return Double.parseDouble(getFormat().format(result));
    }

    public static double getCartesianY(double polarRadius, double polarAngle) {
        double polarAngleRadian = Math.toRadians(polarAngle);
        double result = polarRadius * Math.sin(polarAngleRadian);
        return Double.parseDouble(getFormat().format(result));
    }

    private static DecimalFormat getFormat() {
        return new DecimalFormat("###.###");
    }
}
