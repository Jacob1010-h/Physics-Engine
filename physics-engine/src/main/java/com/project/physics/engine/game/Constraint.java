package com.project.physics.engine.game;

import com.project.physics.engine.math.Vector2f;

public abstract class Constraint {

    private Vector2f center;

    public Constraint(float x, float y) {
        this(new Vector2f(x, y));
    }

    public Constraint(Vector2f center) {
        this.center = center;
    }

    public Vector2f getCenter() {
        return center;
    }
}
