package com.halas.service.rest.wrapper;

import com.halas.model.Position;

public class IntPositionWrapper {
    private int id;
    private Position position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
