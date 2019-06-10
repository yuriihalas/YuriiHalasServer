package com.halas.consts;

import com.halas.model.Position;

public class CopterConstCommon {
    public static final String NOT_EXIST_COPTER = "Copter with id %d not exist.";
    public static final double DISTANCE_MAX = 100;
    public static final double MOVE_COPTER_STEP = 15;
    public static final Position MY_POS = new Position(0, 0, 0);

    private CopterConstCommon() {
    }

}
