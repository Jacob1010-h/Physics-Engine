package com.project.physics.engine.game;

import org.pmw.tinylog.Logger;

import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.state.PongGameState;
import com.project.physics.engine.state.State.Collision;

public class PhysicsEntity extends Entity {

    private float gravityAdder = 0.75f;

    public PhysicsEntity(Color color, Texture texture, float x, float y, float speed) {
        super(color, texture, x, y, speed, 20, 20, 20, 40);

        direction.x = 0;
        direction.y = (float) -Math.sin(Math.toRadians(45.0));
    }

    @Override
    public void input(Entity entity) {
        // Nothing to do here yet
    }

    public PongGameState.Collision checkBorderCollision(int gameWidth, int gameHeight) {
        if (position.y < 0) {
            position.y = 0;
            direction.y = -direction.y;
            changeSpeed(speed-70f);
            Logger.info("bottom collision: "+speed);
            return Collision.COLLISION_BOTTOM;
        }
        if (position.y > gameHeight - this.height) {
            position.y = gameHeight - this.height;
            direction.y = -direction.y;
            Logger.info("top collision: " + speed);
            return Collision.COLLISION_TOP;
        }
        return Collision.NO_COLLISION;
    }
    
}