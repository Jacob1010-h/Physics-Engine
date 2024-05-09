package com.project.physics.engine.game;

import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.graphic.window.DynamicRenderer;
import com.project.physics.engine.math.Vector2f;

public abstract class CenteredEntity {
    
    protected Vector2f previousPosition;
    protected Vector2f position;

    protected Vector2f direction;

    private Vector2f acceleration = new Vector2f();

    protected final Color color;
    protected final Texture texture;

    protected final int radius;

    protected final int tx;
    protected final int ty;

    public CenteredEntity(Color color, Texture texture, float x, float y, int radius, int tx, int ty) {
        previousPosition = new Vector2f(x + radius, y + radius);
        position = new Vector2f(x + radius, y + radius);

        direction = new Vector2f();

        this.color = color;
        this.texture = texture;

        this.radius = radius;

        this.tx = tx;
        this.ty = ty;
    }

    /**
     * Handles input of the entity.
     */
    public void input() {
        input(null);
    }

    public abstract void input(CenteredEntity entity);

    public void update(float delta) {
        Vector2f displacement = position.subtract(previousPosition);
        previousPosition = new Vector2f(position.x, position.y);

        position = position.add(displacement).add(acceleration.scale(delta * delta));

        acceleration = new Vector2f();
    }

    public void render(DynamicRenderer renderer, float alpha) {
        Vector2f interpolatedPosition = previousPosition.lerp(position, alpha);
        float x = interpolatedPosition.x;
        float y = interpolatedPosition.y;
        renderer.drawTextureRegion(texture, x-radius, y-radius, tx, ty, radius * 2f, radius * 2f, color);
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getRadius() {
        return radius;
    }

}
