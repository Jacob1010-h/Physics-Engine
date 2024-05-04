package com.project.physics.engine.game;

import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.math.Vector2f;
import com.project.physics.engine.state.PongGameState;
import com.project.physics.engine.state.State.Collision;

public class PhysicsEntity extends Entity {

    private final Vector2f initVelocity =  new Vector2f(5.0f, 2f);
    private Vector2f velocity = initVelocity;

    public PhysicsEntity(Color color, Texture texture, float x, float y, float speed) {
        super(color, texture, x, y, speed, 20, 20, 20, 40);
    }

    @Override
    public void input(Entity entity) {
        // Nothing to do here yet
    }

    @Override
    public void update(float delta) {
        previousPosition = new Vector2f(position.x, position.y);
        velocity = velocity.add(new Vector2f(0.0f, -9.80f).scale(delta));

        velocity = applyDrag(velocity);

        position = position.add(velocity);

        boundingBox.min.x = position.x;
        boundingBox.min.y = position.y;
        boundingBox.max.x = position.x + width;
        boundingBox.max.y = position.y + height;

    }
    
    private Vector2f applyDrag(Vector2f velocity) {
        float dragCoeff = 0.5f;
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

        if (position.y < 0) {
            position.y = 0;
            velocity = new Vector2f(velocity.x, -velocity.y);
        }
        if (position.y > gameHeight - this.height) {
            position.y = gameHeight - this.height;
            velocity = new Vector2f(velocity.x, -velocity.y);
        }
        if (position.x < 0) {
            position.x = 0;
            velocity = new Vector2f(-velocity.x, velocity.y);
        }
        if (position.x > gameWidth - this.width) {
            position.x = gameWidth - this.width;
            velocity = new Vector2f(-velocity.x, velocity.y);
        }

        return Collision.NO_COLLISION;
    }
    
    // This calculates the speeds for two object in a perfectly elastic collision
    private Vector2f[] calculateCollisionVelocity(float m1, float m2, Vector2f v1Init, Vector2f v2Init) {
        return new Vector2f[] {
            v1Init.scale(((m1 - m2) / (m1 + m2))).add(v2Init.scale(((2 * m2) / (m1
                        - m2)))),
            v1Init.scale(((2 * m1) / (m1 + m2))).add(v2Init.scale(((m2 - m1) / (m1 + m2))))
        };
    }
}