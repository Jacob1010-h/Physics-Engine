package com.project.physics.engine.game;

import com.project.physics.engine.math.Vector2f;

public class CircleConstraint extends Constraint {
    private float radius;

    public CircleConstraint(float x, float y, float radius) {
        this(new Vector2f(x, y), radius);
    }

    public CircleConstraint(Vector2f center, float radius) {
        super(center);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    
}
