package com.halas.dao;

import com.halas.exeption.MaximumDistanceExceededException;
import com.halas.model.Copter;
import com.halas.model.Position;

import java.util.List;

public interface CommonCopterDAO {
    boolean createCopter(Copter copter);

    List<Copter> getAllCopters();

    boolean deleteCopterById(int id);

    boolean changePositionById(int id, Position newPosition) throws MaximumDistanceExceededException;

    boolean goUp(int idCopter) throws MaximumDistanceExceededException;

    boolean goDown(int idCopter) throws MaximumDistanceExceededException;

    boolean goByDegree(int idCopter, double degree) throws MaximumDistanceExceededException;

    boolean holdPosition(int idCopter);
}
