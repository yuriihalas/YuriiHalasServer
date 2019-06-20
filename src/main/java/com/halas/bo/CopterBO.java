package com.halas.bo;

import com.halas.dao.CopterDAO;
import com.halas.exeption.MaximumDistanceExceededException;
import com.halas.model.Copter;
import com.halas.model.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

import static com.halas.consts.CopterConstCommon.MOVE_COPTER_STEP;
import static com.halas.consts.CopterConstCommon.NOT_EXIST_COPTER;
import static com.halas.utils.JsonParser.readAllCopters;

public class CopterBO {
    private static final Logger LOG = LogManager.getLogger(CopterBO.class);
    private CopterDAO copterDao;

    public CopterBO() {
        copterDao = new CopterDAO();
    }

    public boolean insertCopter(Copter copter) {
        LOG.info("method insertCopter..");
        return copterDao.createCopter(copter);
    }

    public List<Copter> getAllCopters() {
        LOG.info("method getAllCopters..");
        return copterDao.getAllCopters();
    }

    public boolean deleteCopter(int id) {
        LOG.info("method deleteCopter..");
        return copterDao.deleteCopterById(id);
    }

    public boolean updatePositionById(int id, Position newPosition) throws MaximumDistanceExceededException {
        LOG.info("method updatePositionById..");
        return copterDao.changePositionById(id, newPosition);
    }

    public boolean moveByDegree(int idCopter, double degree) throws MaximumDistanceExceededException {
        LOG.info("method moveByDegree..");
        return copterDao.goByDegree(idCopter, degree);
    }

    public boolean moveUp(int idCopter) throws MaximumDistanceExceededException {
        LOG.info("method moveUp..");
        List<Copter> copters = readAllCopters();
        Copter copterForMove = copterDao.findCopterById(copters, idCopter);
        if (Objects.nonNull(copterForMove)) {
            double newZ = copterForMove.getPosition().getCoordinateZ() + MOVE_COPTER_STEP;
            Position newPosition = copterForMove.getPosition();
            newPosition.setCoordinateZ(newZ);
            copterDao.changePosition(copters, copterForMove, newPosition);
            LOG.info(String.format("Successfully went up copter with id: %s!", idCopter));
            return true;
        }
        LOG.warn(String.format(NOT_EXIST_COPTER, idCopter));
        return false;

    }

    public boolean moveDown(int idCopter) throws MaximumDistanceExceededException {
        LOG.info("method moveDown..");
        List<Copter> copters = readAllCopters();
        Copter copterForMove = copterDao.findCopterById(copters, idCopter);
        if (Objects.nonNull(copterForMove)) {
            double newZ = copterForMove.getPosition().getCoordinateZ() - MOVE_COPTER_STEP;
            Position newPosition = copterForMove.getPosition();
            newPosition.setCoordinateZ(newZ);
            copterDao.changePosition(copters, copterForMove, newPosition);
            LOG.info(String.format("Successfully went down copter with id: %s!", idCopter));
            return true;
        }
        LOG.warn(String.format(NOT_EXIST_COPTER, idCopter));
        return false;
    }

    public boolean standStill(int idCopter) {
        LOG.info("method standStill");
        List<Copter> copters = readAllCopters();
        Copter copter = copterDao.findCopterById(copters, idCopter);
        return Objects.nonNull(copter);
    }

    public Copter findCopterById(int idCopter) {
        LOG.info("method findCopterById");
        List<Copter> copters = readAllCopters();
        return copterDao.findCopterById(copters, idCopter);
    }
}
