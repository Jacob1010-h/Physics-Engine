package com.project.physics.engine.game;

import org.pmw.tinylog.Logger;

import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.math.Vector2f;
import com.project.physics.engine.state.PongGameState;
import com.project.physics.engine.state.State.Collision;

public class PhysicsEntity extends Entity {

    private Vector2f velocity = new Vector2f(0.0f, 250.0f);

    public PhysicsEntity(Color color, Texture texture, float x, float y, float speed) {
        super(color, texture, x, y, speed, 20, 20, 20, 40);

        direction.x = 0;
        direction.y = (float) -Math.sin(Math.toRadians(45.0));
    }

    @Override
    public void input(Entity entity) {
        // Nothing to do here yet
    }

    @Override
    public void update(float delta) {
        previousPosition = new Vector2f(position.x, position.y);
        if (direction.length() != 0) {
            direction = direction.normalize();
            
        }
        velocity = velocity.add(direction.scale(speed));
        Logger.info(velocity.y);
        position = position.add(velocity.scale(delta));
        
        if (velocity.y > 0)
            velocity = velocity.negate();

        boundingBox.min.x = position.x;
        boundingBox.min.y = position.y;
        boundingBox.max.x = position.x + width;
        boundingBox.max.y = position.y + height;
    }

    public PongGameState.Collision checkBorderCollision(int gameWidth, int gameHeight) {
        if (position.y < 0) {
            position.y = 0;
            direction.y = -direction.y;
            if (velocity.y < -500f)
                velocity = velocity.add(direction.scale(speed)).negate();
            
            return Collision.COLLISION_BOTTOM;
        }
        if (position.y > gameHeight - this.height) {
            position.y = gameHeight - this.height;
            direction.y = -direction.y;
            return Collision.COLLISION_TOP;
        }
        return Collision.NO_COLLISION;
    }
    
}