package com.halas.service.rest;

import com.halas.bo.CopterBO;
import com.halas.model.Copter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/copters/")
public class CopterREST {
    private CopterBO copterBO;

    public CopterREST() {
        copterBO = new CopterBO();
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<List<Copter>> getAllCopters() {
        List<Copter> copters = copterBO.getAllCopters();

        if (copters.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(copters, HttpStatus.OK);
    }
}
