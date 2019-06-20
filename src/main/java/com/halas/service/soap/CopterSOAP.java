package com.halas.service.soap;

import com.halas.bo.CopterBO;
import com.halas.exeption.DuplicateCopterIdException;
import com.halas.exeption.MaximumDistanceExceededException;
import com.halas.exeption.NoSuchCopterIdException;
import com.halas.model.Copter;
import com.halas.model.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jws.WebService;
import java.util.List;
import java.util.Objects;


@WebService(endpointInterface = "com.halas.service.soap.CopterServiceSOAP")
public class CopterSOAP implements CopterServiceSOAP {
    private static final Logger LOG = LogManager.getLogger(CopterSOAP.class);
    private CopterBO copterBo;

    public CopterSOAP() {
        copterBo = new CopterBO();
    }

    @Override
    public boolean addCopter(Copter copter) throws DuplicateCopterIdException {
        LOG.info("method addCopter.");
        if (!copterBo.insertCopter(copter)) {
            LOG.warn(DuplicateCopterIdException.class.getSimpleName());
            throw new DuplicateCopterIdException();
        }
        LOG.info("Successfully added copter!");
        return true;
    }

    @Override
    public List<Copter> showAllCopters() {
        LOG.info("method showAllCopters.");
        return copterBo.getAllCopters();
    }

    @Override
    public boolean deleteCopterById(int id) throws NoSuchCopterIdException {
        LOG.info("method deleteCopterById.");
        if (!copterBo.deleteCopter(id)) {
            LOG.warn(NoSuchCopterIdException.class.getSimpleName());
            throw new NoSuchCopterIdException();
        }
        LOG.info("Successfully deleted copter with id: " + id);
        return true;
    }

    @Override
    public boolean moveToPositionById(int id, Position newPosition)
            throws MaximumDistanceExceededException, NoSuchCopterIdException {
        LOG.info("method moveToPositionById.");
        if (!copterBo.updatePositionById(id, newPosition)) {
            LOG.warn(NoSuchCopterIdException.class.getSimpleName());
            throw new NoSuchCopterIdException();
        }
        LOG.info(String.format("Successfully moved copter with id = %d position to: %s", id, newPosition));
        return true;
    }

    @Override
    public boolean goUp(int idCopter)
            throws MaximumDistanceExceededException, NoSuchCopterIdException {
        LOG.info("method goUp.");
        if (!copterBo.moveUp(idCopter)) {
            LOG.warn(NoSuchCopterIdException.class.getSimpleName());
            throw new NoSuchCopterIdException();
        }
        LOG.info(String.format("Successfully went up copter with id: %d", idCopter));
        return true;
    }

    @Override
    public boolean goDown(int idCopter)
            throws MaximumDistanceExceededException, NoSuchCopterIdException {
        LOG.info("method goDown.");
        if (!copterBo.moveDown(idCopter)) {
            LOG.warn(NoSuchCopterIdException.class.getSimpleName());
            throw new NoSuchCopterIdException();
        }
        LOG.info(String.format("Successfully went down copter with id: %d", idCopter));
        return true;
    }

    @Override
    public boolean goByDegree(int idCopter, double degree)
            throws MaximumDistanceExceededException, NoSuchCopterIdException {
        LOG.info("method goByDegree.");
        if (!copterBo.moveByDegree(idCopter, degree)) {
            LOG.warn(NoSuchCopterIdException.class.getSimpleName());
            throw new NoSuchCopterIdException();
        }
        LOG.info(String.format("Successfully moved copter with id: %d and degree: %f", idCopter, degree));
        return true;
    }

    @Override
    public boolean holdPosition(int idCopter)
            throws NoSuchCopterIdException {
        LOG.info("method holdPosition.");
        if (!copterBo.standStill(idCopter)) {
            LOG.warn(NoSuchCopterIdException.class.getSimpleName());
            throw new NoSuchCopterIdException();
        }
        LOG.info(String.format("Successfully holdPosition copter with id: %d", idCopter));
        return true;
    }

    @Override
    public Copter findCopter(int idCopter) throws NoSuchCopterIdException {
        LOG.info("method findCopter.");
        Copter foundedCopter = copterBo.findCopterById(idCopter);
        if(Objects.isNull(foundedCopter)){
            LOG.warn(NoSuchCopterIdException.class.getSimpleName());
            throw new NoSuchCopterIdException();
        }
        LOG.info(String.format("Successfully founded copter with id: %d", idCopter));
        return foundedCopter;
    }
}
