package com.halas.service.rest;

import com.halas.bo.CopterBO;
import com.halas.exeption.MaximumDistanceExceededException;
import com.halas.model.Copter;
import com.halas.model.Position;
import com.halas.service.rest.wrapper.IntPositionWrapper;
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(copters, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCopter(@RequestBody @Valid Copter copter) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (Objects.isNull(copter)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (copterBO.insertCopter(copter)) {
            return new ResponseEntity<>(SUCCESS_ACTION, httpHeaders, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(String.format(ID_EXIST_BAD_FORMAT, copter.getId()),
                httpHeaders,
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCopter(@PathVariable("id") Integer id) {
        if (copterBO.deleteCopter(id)) {
            return new ResponseEntity<>(SUCCESS_ACTION, HttpStatus.OK);
        }
        return new ResponseEntity<>(String.format(FAILURE_FORMAT, ID_NOT_EXISTS), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> moveToPositionById(@RequestBody @Valid IntPositionWrapper wrapper) {
        int id = wrapper.getId();
        Position newPosition = wrapper.getPosition();
        try {
            if (copterBO.updatePositionById(id, newPosition)) {
                return new ResponseEntity<>(SUCCESS_ACTION, HttpStatus.OK);
            }
        } catch (MaximumDistanceExceededException e) {
            outputError(LOG, e);
            return new ResponseEntity<>(String.format(FAILURE_FORMAT, MAXIMUM_DISTANCE_EXECUTED), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(String.format(FAILURE_FORMAT, ID_NOT_EXISTS), HttpStatus.CONFLICT);
    }
}
