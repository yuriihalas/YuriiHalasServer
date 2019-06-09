package com.halas.model;

import java.util.Objects;

public class Position {
    private double coordinateX;
    private double coordinateY;
    private double coordinateZ;

    public Position() {
    }

    public Position(double coordinateX, double coordinateY, double coordinateZ) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.coordinateZ = coordinateZ;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(double coordinateX) {
        this.coordinateX = coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(double coordinateY) {
        this.coordinateY = coordinateY;
    }

    public double getCoordinateZ() {
        return coordinateZ;
    }

    public void setCoordinateZ(double coordinateZ) {
        this.coordinateZ = coordinateZ;
    }

    public double getDistance(Position position){
        return Math.sqrt(Math.pow(coordinateX - position.getCoordinateX(), 2) +
                Math.pow(coordinateY - position.getCoordinateY(), 2) +
                Math.pow(coordinateZ - position.getCoordinateZ(), 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position that = (Position) o;
        return Double.compare(that.coordinateX, coordinateX) == 0 &&
                Double.compare(that.coordinateY, coordinateY) == 0 &&
                Double.compare(that.coordinateZ, coordinateZ) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinateX, coordinateY, coordinateZ);
    }

    @Override
    public String toString() {
        return "CopterRemoteControl{" +
                "coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                ", coordinateZ=" + coordinateZ +
                '}';
    }
}
