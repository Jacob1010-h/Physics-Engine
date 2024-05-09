package com.project.physics.engine.game;

import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.math.Vector2f;
import com.project.physics.engine.state.PongGameState;
import com.project.physics.engine.state.State.Collision;

public class PhysicsEntity extends CenteredEntity {

    private Vector2f velocity = new Vector2f();
    
    private final int radius;

    private final float mass;

    // ? This class should be allowed to render multiple balls at once but wont when put into the physics engine?
    public PhysicsEntity(Color color, Texture texture, float x, float y, int radius,
            float mass) {
        super(color, texture, x, y, radius, 20, 40);

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

        velocity = velocity.add(new Vector2f(0.0f, -9.80f).scale(delta));
        position = position.add(velocity);
        System.out.println(velocity);
        // velocity = applyDrag(velocity);
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

    public PongGameState.Collision checkBorderCollision(int gameWidth, int gameHeight) {
        // Logger.info(velocity.y);
        Vector2f center = new Vector2f(gameWidth / 2f, gameHeight / 2f);
        Vector2f distanceVector = center.subtract(position);
        float distance = distanceVector.length();
        if (distance > (200f - radius)) {
            Vector2f normal = distanceVector.divide(distance);
            position = center.subtract(normal.scale(200f - radius));
            //TODO: compute the bounce angle velocity
            velocity = (velocity.scale(velocity).add(new Vector2f(0f, -9.5f).scale(normal).scale(2))).sqrt();
            
        }

        return Collision.NO_COLLISION;
    }
    
    public boolean hasCollided(PhysicsEntity other) {
        return position.subtract(other.position).abs().length() < (this.radius + other.radius);
    }
    
    // This calculates the speeds for two object in a perfectly elastic collision
    // public ElasticCollisionResults calculateCollisionVelocity(PhysicsEntity other) {
    //     Vector2f firstResult = this.velocity.scale(((this.mass - other.mass) / (this.mass + other.mass)))
    //             .add(other.velocity.scale(2f).scale((((other.mass) / (this.mass
    //                     + other.mass)))));

    //     Vector2f secondResult = this.velocity.scale(2f).scale(((this.mass) / (this.mass + other.mass)))
    //             .subtract((other.velocity.scale(((this.mass - other.mass) / (this.mass + other.mass)))));

    //     return new ElasticCollisionResults(firstResult, secondResult);
    // }
    
    public record ElasticCollisionResults(Vector2f first, Vector2f second) {
        public ElasticCollisionResults() {
            this(new Vector2f(), new Vector2f());
        }

        public ElasticCollisionResults(Vector2f[] results) {
            this(results[0], results[1]);
        }

        @Override
        public final String toString() {
            return "First: "+first.toString()+", Second: "+second.toString();
        }
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }
    
}