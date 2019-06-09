package com.halas.bo;

import com.halas.dao.CopterDAO;
import com.halas.exeption.MaximumDistanceExceededException;
import com.halas.model.Copter;
import com.halas.model.Position;

import java.util.List;

public class CopterBO {

    private CopterDAO copterDao;

    public CopterBO() {
        copterDao = new CopterDAO();
    }

    public boolean insertCopter(Copter copter) {
        return copterDao.createCopter(copter);
    }

    public List<Copter> getAllCopters() {
        return copterDao.getAllCopters();
    }

    public boolean deleteCopter(int id) {
        return copterDao.deleteCopterById(id);
    }

    public boolean updatePositionById(int id, Position newPosition) throws MaximumDistanceExceededException {
        return copterDao.changePositionById(id, newPosition);
    }

    public boolean moveUp(int idCopter) throws MaximumDistanceExceededException {
        return copterDao.goUp(idCopter);
    }

    public boolean moveDown(int idCopter) throws MaximumDistanceExceededException {
        return copterDao.goDown(idCopter);
    }

    public boolean moveByDegree(int idCopter, double degree) throws MaximumDistanceExceededException {
        return copterDao.goByDegree(idCopter, degree);
    }

    public boolean standStill(int idCopter) {
        return copterDao.holdPosition(idCopter);
    }
}
