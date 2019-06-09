package com.halas.dao;

import com.halas.exeption.MaximumDistanceExceededException;
import com.halas.model.Copter;
import com.halas.model.Position;
import com.halas.utils.ParserPolarSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

import static com.halas.utils.JsonParser.readAllCopters;
import static com.halas.utils.JsonParser.writeCopterToJson;

public class CopterDAO implements CommonCopterDAO {
    private static final Logger LOG = LogManager.getLogger(CopterDAO.class);
    private static final double DISTANCE_MAX = 100;
    private static final double MOVE_COPTER_STEP = 15;
    private static final Position MY_POS = new Position(0, 0, 0);

    @Override
    public boolean createCopter(Copter newCopter) {
        LOG.info("method creteCopter..");
        List<Copter> copters = readAllCopters();
        boolean isCreated = false;

        if (!copters.contains(newCopter)) {
            copters.add(newCopter);
            writeCopterToJson(copters);
            isCreated = true;
        }
        return isCreated;
    }

    @Override
    public List<Copter> getAllCopters() {
        LOG.info("method readAllCopters..");
        return readAllCopters();
    }

    @Override
    public boolean deleteCopterById(int id) {
        LOG.info("method deleteCopterById..");
        List<Copter> copters = readAllCopters();
        Copter copterDelete = findCopterById(copters, id);
        if (Objects.nonNull(copterDelete)) {
            copters.remove(copterDelete);
            writeCopterToJson(copters);
            LOG.info("Successfully deleted copter..");
            return true;
        }
        return false;
    }

    @Override
    public boolean changePositionById(int id, Position newPosition) throws MaximumDistanceExceededException {
        List<Copter> copters = readAllCopters();
        Copter copterChange = findCopterById(copters, id);
        return changePosition(copters, copterChange, newPosition);
    }

    @Override
    public boolean goUp(int idCopter) throws MaximumDistanceExceededException {
        LOG.info("method goUp..");
        List<Copter> copters = readAllCopters();
        Copter copterForMove = findCopterById(copters, idCopter);
        if (Objects.nonNull(copterForMove)) {
            double newZ = copterForMove.getPosition().getCoordinateZ() + MOVE_COPTER_STEP;
            Position newPosition = copterForMove.getPosition();
            newPosition.setCoordinateZ(newZ);
            copterForMove.setPosition(newPosition);
            changePosition(copters, copterForMove, newPosition);
            return true;
        }
        return false;
    }

    @Override
    public boolean goDown(int idCopter) throws MaximumDistanceExceededException {
        LOG.info("method goDown..");
        List<Copter> copters = readAllCopters();
        Copter copterForMove = findCopterById(copters, idCopter);
        if (Objects.nonNull(copterForMove)) {
            double newZ = copterForMove.getPosition().getCoordinateZ() - MOVE_COPTER_STEP;
            Position newPosition = copterForMove.getPosition();
            newPosition.setCoordinateZ(newZ);
            copterForMove.setPosition(newPosition);
            changePosition(copters, copterForMove, newPosition);
            return true;
        }
        return false;
    }

    @Override
    public boolean goByDegree(int idCopter, double degree) throws MaximumDistanceExceededException {
        LOG.info("method goByDegree..");
        List<Copter> copters = readAllCopters();
        Copter copterForMove = findCopterById(copters, idCopter);
        if (Objects.nonNull(copterForMove)) {
            double newX = ParserPolarSystem.getCartesianX(MOVE_COPTER_STEP, degree);
            double newY = ParserPolarSystem.getCartesianY(MOVE_COPTER_STEP, degree);
            Position newPosition = copterForMove.getPosition();
            newPosition.setCoordinateX(newX);
            newPosition.setCoordinateY(newY);
            copterForMove.setPosition(newPosition);
            changePosition(copters, copterForMove, newPosition);
            return true;
        }
        return false;
    }

    @Override
    public boolean holdPosition(int idCopter) {
        LOG.info("method holdPosition");
        List<Copter> copters = readAllCopters();
        Copter copter = findCopterById(copters, idCopter);
        return Objects.nonNull(copter);
    }

    private boolean changePosition(List<Copter> copters, Copter copterChange, Position newPosition)
            throws MaximumDistanceExceededException {
        if (Objects.nonNull(copterChange)) {
            if (!isCopterAbleToMoveNewPoss(MY_POS, newPosition)) {
                String mess = String.format("Can't fly so far, max distance is %s!", DISTANCE_MAX);
                LOG.warn(mess);
                throw new MaximumDistanceExceededException(mess);
            }
            copterChange.setPosition(newPosition);
            writeCopterToJson(copters);
            LOG.info("Successfully changed position..");
            return true;
        }
        return false;
    }

    private boolean isCopterAbleToMoveNewPoss(Position myPosition, Position positionCopter) {
        double distance = myPosition.getDistance(positionCopter);
        return distance > DISTANCE_MAX;
    }

    private Copter findCopterById(List<Copter> copters, int idCopter) {
        return copters.stream().filter(c -> c.getId().equals(idCopter)).findFirst().orElse(null);
    }
}
