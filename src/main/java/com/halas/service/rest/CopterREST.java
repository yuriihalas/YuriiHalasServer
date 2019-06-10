package com.halas.service.rest;

import com.halas.bo.CopterBO;
import com.halas.exeption.MaximumDistanceExceededException;
import com.halas.model.Copter;
import com.halas.model.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<List<Copter>> getAllCopters() {
        List<Copter> copters = copterBO.getAllCopters();
        if (copters.isEmpty()) {
            LOG.warn(EMPTY_LIST_COPTERS);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LOG.info(SUCCESS_ACTION);
        return new ResponseEntity<>(copters, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Copter> createCopter(@RequestBody @Valid Copter copter) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (Objects.isNull(copter)) {
            LOG.warn(String.format(FAILURE_FORMAT, COPTER_IS_NULL));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (copterBO.insertCopter(copter)) {
            LOG.info(SUCCESS_ACTION);
            return new ResponseEntity<>(copter, httpHeaders, HttpStatus.CREATED);
        }
        LOG.warn(String.format(ID_EXIST_BAD_FORMAT, copter.getId()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @RequestMapping(value = "{copter-id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Copter> deleteCopter(@PathVariable("copter-id") Integer id) {
        Copter copterDeleted = copterBO.findCopterById(id);
        if (copterBO.deleteCopter(id)) {
            LOG.info(SUCCESS_ACTION);
            return new ResponseEntity<>(copterDeleted, HttpStatus.OK);
        }
        LOG.warn(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{copter-id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Position> moveToPositionById(
            @PathVariable("copter-id") Integer id,
            @RequestBody @Valid Position newPosition) {
        try {
            if (copterBO.updatePositionById(id, newPosition)) {
                LOG.info(SUCCESS_ACTION);
                return new ResponseEntity<>(newPosition, HttpStatus.OK);
            }
        } catch (MaximumDistanceExceededException e) {
            outputError(LOG, e);
            LOG.warn(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        LOG.warn(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{copter-id}/{direction-degree}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Position> moveToPositionByIdWithDegree(
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
            LOG.warn(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        LOG.warn(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{copter-id}/move-up", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Position> moveUp(@PathVariable("copter-id") Integer id) {
        try {
            if (copterBO.moveUp(id)) {
                Position position = copterBO.findCopterById(id).getPosition();
                LOG.info(SUCCESS_ACTION);
                return new ResponseEntity<>(position, HttpStatus.OK);
            }
        } catch (MaximumDistanceExceededException e) {
            outputError(LOG, e);
            LOG.warn(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        LOG.warn(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{copter-id}/move-down", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Position> moveDown(@PathVariable("copter-id") Integer id) {
        try {
            if (copterBO.moveDown(id)) {
                Position position = copterBO.findCopterById(id).getPosition();
                LOG.info(SUCCESS_ACTION);
                return new ResponseEntity<>(position, HttpStatus.OK);
            }
        } catch (MaximumDistanceExceededException e) {
            outputError(LOG, e);
            LOG.warn(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        LOG.warn(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "{copter-id}/hold-position", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> holdPosition(@PathVariable("copter-id") Integer id) {
        if (copterBO.standStill(id)) {
            LOG.info(SUCCESS_ACTION);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        LOG.warn(String.format(FAILURE_FORMAT, ID_NOT_EXISTS));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
