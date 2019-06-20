package com.halas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CopterArray implements Serializable {

    private List<Copter> copters;

    public CopterArray(List<Copter> newItem) {
        copters = newItem;
    }

    public List<Copter> getCopters() {
        if (copters == null) {
            copters = new ArrayList<>();
        }
        return this.copters;
    }
}