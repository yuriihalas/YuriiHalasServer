package com.halas.service.rest;

import com.halas.bo.CopterBO;
import com.halas.exeption.MaximumDistanceExceededException;
import com.halas.model.Copter;
import com.halas.model.CopterArray;
import com.halas.model.Position;
import com.halas.service.rest.error.CustomError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static com.halas.service.rest.consts.ConstCopterREST.*;
import static com.halas.utils.ErrorHelper.outputError;

@RestController
@RequestMapping("/api/copters/")
public class CopterREST {
    private static final Logger LOG = LogManager.getLogger(CopterREST.class);
    private CopterBO copterBO;

    public CopterREST() {
        copterBO = new CopterBO();
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllCopters() {
        List<Copter> copters = copterBO.getAllCopters();
        CopterArray copterArray = new CopterArray(copters);
        if (copters.isEmpty()) {
            LOG.error(EMPTY_LIST_COPTERS);
            CustomError customError = new CustomError(EMPTY_LIST_COPTERS);
            return new ResponseEntity<>(customError, HttpStatus.NO_CONTENT);
        }
        LOG.info(SUCCESS_ACTION);
        return new ResponseEntity<>(copterArray, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "{copter-id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findCopter(@PathVariable("copter-id") Integer id) {
        Copter copter = copterBO.findCopterById(id);
        if (Objects.isNull(copter)) {
            LOG.error(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
            CustomError customError = new CustomError(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
            return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
        }
        LOG.info(SUCCESS_ACTION);
        return new ResponseEntity<>(copter, HttpStatus.FOUND);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createCopter(@RequestBody @Valid Copter copter) {
        if (Objects.isNull(copter)) {
            LOG.error(String.format(FAILURE_FORMAT, COPTER_IS_NULL));
            CustomError error = new CustomError(String.format(FAILURE_FORMAT, COPTER_IS_NULL));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (copterBO.insertCopter(copter)) {
            LOG.info(SUCCESS_ACTION);
            return new ResponseEntity<>(copter, HttpStatus.CREATED);
        }
        LOG.error(String.format(ID_EXIST_BAD_FORMAT, copter.getId()));
        CustomError error = new CustomError(String.format(ID_EXIST_BAD_FORMAT, copter.getId()));
        return new ResponseEntity<>(error, HttpStatus.SEE_OTHER);
    }

    @RequestMapping(value = "{copter-id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteCopter(@PathVariable("copter-id") Integer id) {
        Copter copterDeleted = copterBO.findCopterById(id);
        if (copterBO.deleteCopter(id)) {
            LOG.info(SUCCESS_ACTION);
            return new ResponseEntity<>(copterDeleted, HttpStatus.OK);
        }
        LOG.error(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        CustomError error = new CustomError(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{copter-id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> moveToPositionById(
            @PathVariable("copter-id") Integer id,
            @RequestBody @Valid Position newPosition) {
        try {
            if (copterBO.updatePositionById(id, newPosition)) {
                LOG.info(SUCCESS_ACTION);
                return new ResponseEntity<>(newPosition, HttpStatus.OK);
            }
        } catch (MaximumDistanceExceededException e) {
            outputError(LOG, e);
            LOG.error(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            CustomError error = new CustomError(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
        LOG.error(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        CustomError error = new CustomError(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{copter-id}/{direction-degree}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> moveToPositionByIdWithDegree(
            @PathVariable("copter-id") Integer id,
            @PathVariable("direction-degree") Double degree) {
        try {
            if (copterBO.moveByDegree(id, degree)) {
                Position position = copterBO.findCopterById(id).getPosition();
                LOG.info(SUCCESS_ACTION);
                return new ResponseEntity<>(position, HttpStatus.OK);
            }
        } catch (MaximumDistanceExceededException e) {
            outputError(LOG, e);
            LOG.error(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            CustomError error = new CustomError(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
        LOG.error(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        CustomError error = new CustomError(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{copter-id}/move-up", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> moveUp(@PathVariable("copter-id") Integer id) {
        try {
            if (copterBO.moveUp(id)) {
                Position position = copterBO.findCopterById(id).getPosition();
                LOG.info(SUCCESS_ACTION);
                return new ResponseEntity<>(position, HttpStatus.OK);
            }
        } catch (MaximumDistanceExceededException e) {
            outputError(LOG, e);
            LOG.error(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            CustomError error = new CustomError(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
        LOG.error(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        CustomError error = new CustomError(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{copter-id}/move-down", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> moveDown(@PathVariable("copter-id") Integer id) {
        try {
            if (copterBO.moveDown(id)) {
                Position position = copterBO.findCopterById(id).getPosition();
                LOG.info(SUCCESS_ACTION);
                return new ResponseEntity<>(position, HttpStatus.OK);
            }
        } catch (MaximumDistanceExceededException e) {
            outputError(LOG, e);
            LOG.error(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            CustomError error = new CustomError(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
        LOG.error(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        CustomError error = new CustomError(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{copter-id}/hold-position", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> holdPosition(@PathVariable("copter-id") Integer id) {
        if (copterBO.standStill(id)) {
            Copter copter = copterBO.findCopterById(id);
            LOG.info(SUCCESS_ACTION);
            return new ResponseEntity<>(copter, HttpStatus.OK);
        }
        LOG.error(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        CustomError error = new CustomError(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
