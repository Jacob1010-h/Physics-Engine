package com.project.physics.engine.game;

import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.math.Vector2f;

public class PhysicsEntity extends CenteredEntity {

    private Vector2f velocity = new Vector2f();

    private final int radius;

    private final float mass;

    // ? This class should be allowed to render multiple balls at once but wont when
    // put into the physics engine?
    public PhysicsEntity(Color color, Texture texture, float startX, float startY, int radius,
            float mass) {
        super(color, texture, startX, startY, radius, 20, 40);

        this.mass = mass;

        this.radius = radius;
    }

    @Override
    public void input(CenteredEntity entity) {
        // Nothing to do here yet
    }

    @Override
    public void update(float delta) {
        previousPosition = new Vector2f(position.x, position.y);

        if (!velocity.partIsZero())
            velocity.normalize();

        velocity = applyGravity(new Vector2f(0.0f, -9.80f), delta);
        velocity = applyDrag(velocity);
        position = position.add(velocity);
    }

    private Vector2f applyGravity(Vector2f gravity, float delta) {
        return velocity.add(gravity.scale(delta));
    }

    private Vector2f applyDrag(Vector2f velocity) {
        float dragCoeff = 0.8f;
        float airDensity = 1.2f / 100f;
        Vector2f calculatedDrag = velocity.scale(velocity).scale(dragCoeff).scale(airDensity).divide(2f);
        if (velocity.y < 0) {
            velocity = new Vector2f(velocity.x, velocity.y + calculatedDrag.y);
        } else if (velocity.y > 0) {
            velocity = new Vector2f(velocity.x, velocity.y - calculatedDrag.y);
        }
        if (velocity.x < 0) {
            velocity = new Vector2f(velocity.x + calculatedDrag.x, velocity.y);
        } else if (velocity.x > 0) {
            velocity = new Vector2f(velocity.x - calculatedDrag.x, velocity.y);
        }
        return velocity;
    }

    public void checkBorderCollision(int gameWidth, int gameHeight) {
        Vector2f center = new Vector2f(gameWidth / 2f, gameHeight / 2f);
        Vector2f distanceVector = center.subtract(position);
        float distance = distanceVector.length();
        if (distance > (200f - radius)) {
            Vector2f normal = distanceVector.divide(distance);
            position = center.subtract(normal.scale(200f - radius));
            velocity = velocity.bounce(normal);
        }
    }

    public boolean hasCollided(PhysicsEntity other) {
        return position.subtract(other.position).abs().length() < (this.radius + other.radius);
    }

    public ElasticCollisionResults calculateCollisionVelocity(PhysicsEntity other) {
        Vector2f distance = this.position.subtract(other.position);
        Vector2f normal = distance.divide(distance.length());

        float minDistance = this.radius + other.radius;

        float massRatio1 = this.mass / other.mass;
        float massRatio2 = other.mass / this.mass;
        float delta = 0.5f * 0.75f * (distance.length() - minDistance);

        Vector2f firstPosition = this.position.subtract(normal.scale(massRatio2 * delta));
        Vector2f secondPosition = other.position.add(normal.scale(massRatio1 * delta));
        
        Vector2f firstVelocity = this.velocity.scale(((this.mass - other.mass) / (this.mass + other.mass)))
                .add(other.velocity.scale(2f).scale((((other.mass) / (this.mass
                        + other.mass)))));
        Vector2f secondVelocity = this.velocity.scale(2f).scale(((this.mass) / (this.mass + other.mass)))
                .subtract((other.velocity.scale(((this.mass - other.mass) / (this.mass + other.mass)))));

        return new ElasticCollisionResults(firstPosition, firstVelocity, secondPosition, secondVelocity);
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public record ElasticCollisionResults(Vector2f firstPosition, Vector2f firstVelocity, Vector2f secondPosition,
            Vector2f secondVelocity) {
        public ElasticCollisionResults() {
            this(new Vector2f(), new Vector2f(), new Vector2f(), new Vector2f());
        }

        public ElasticCollisionResults(Vector2f[] results) {
            this(results[0], results[1], results[2], results[3]);
        }

        @Override
        public final String toString() {
            return "First: " + firstPosition.toString() + ", Second: " + secondPosition.toString();
        }
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

}