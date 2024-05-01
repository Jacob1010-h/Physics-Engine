package com.project.physics.engine.components;

public abstract class Component {

    protected final int x;
    protected final int y;
    protected final float width;
    protected final float height;

    public Component(int x, int y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void render();

    public record Color(int r, int g, int b) {
    }
}
