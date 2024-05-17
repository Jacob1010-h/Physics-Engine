package com.project.physics.engine.game;

import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.math.Vector2f;

public class PhysicsEntity extends CenteredEntity {

    private final int radius;

    // ? This class should be allowed to render multiple balls at once but wont when
    // put into the physics engine?
    public PhysicsEntity(Color color, Texture texture, float startX, float startY, int radius,
            float mass) {
        super(color, texture, startX, startY, radius, 20, 40);

        this.radius = radius;
    }

    @Override
    public void input(CenteredEntity entity) {
        // Nothing to do here yet
    }

    @Override
    public void update(float delta) {
        Vector2f difference = position.subtract(previousPosition);

        previousPosition = position;
        
        position = position.add(difference).add(new Vector2f(0.0f, -150f).scale(delta*delta));
    }

    public void checkBorderCollision(int gameWidth, int gameHeight) {
        Vector2f center = new Vector2f(gameWidth / 2f, gameHeight / 2f);
        Vector2f distanceVector = center.subtract(position);
        float distance = distanceVector.length();
        if (distance > (200f - radius)) {
            Vector2f normal = distanceVector.divide(distance);
            position = center.subtract(normal.scale(200f - radius));
        }
    }

    public boolean hasCollided(PhysicsEntity other) {
        return position.subtract(other.position).abs().length() < (this.radius + other.radius);
    }

    public PhysicalCollisionResults calculateCollisionVelocity(PhysicsEntity other) {
        Vector2f distance = this.position.subtract(other.position);
        Vector2f normal = distance.divide(distance.length());

        float minDistance = this.radius + other.radius;

        float massRatio1 = this.radius / (float) (this.radius + other.radius);
        float massRatio2 = other.radius / (float) (this.radius + other.radius);
        float delta = 0.5f * 0.75f * (distance.length() - minDistance);

        Vector2f first = this.position.subtract(normal.scale(massRatio2 * delta));
        Vector2f second = other.position.add(normal.scale(massRatio1 * delta));

        return new PhysicalCollisionResults(first, second);
    }

    public record PhysicalCollisionResults(Vector2f first, Vector2f second) {
        public PhysicalCollisionResults() {
            this(new Vector2f(), new Vector2f());
        }

        public PhysicalCollisionResults(Vector2f[] results) {
            this(results[0], results[1]);
            if (results.length != 2) {
                throw new IllegalArgumentException("ElasticCollision Vector Array length must be 2");
            }
        }

        @Override
        public final String toString() {
            return "First: " + first.toString() + ", Second: " + second.toString();
        }
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

}