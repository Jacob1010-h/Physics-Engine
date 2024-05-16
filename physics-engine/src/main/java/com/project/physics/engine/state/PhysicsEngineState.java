package com.project.physics.engine.state;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.glClearColor;
import org.lwjgl.system.MemoryStack;

import com.project.physics.engine.game.PhysicsEntity;
import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.graphic.window.DynamicRenderer;

/**
 *
 * @author jacob
 */
public class PhysicsEngineState implements State {

    private final DynamicRenderer renderer;

    private Texture texture;
    private final ArrayList<PhysicsEntity> physicsEntities = new ArrayList<>();

    private int gameWidth;
    private int gameHeight;


    public PhysicsEngineState(DynamicRenderer renderer) {
        this.renderer = renderer;
        
    }
    
    @Override
    public void input() {
        // There is no input for this yet :))
    }

    @Override
    public void update(float delta) {
        applyConstraints();
        solveCollisions();
        updateAllEntities(delta);
    }

    private void applyConstraints() {
        for (PhysicsEntity physicsEntity : physicsEntities) {
            physicsEntity.checkBorderCollision(gameWidth, gameHeight);
        }
    }

    private void updateAllEntities(float delta) {
        for (PhysicsEntity physicsEntity : physicsEntities) {
            physicsEntity.update(delta);
        }
    }

    private void solveCollisions() {
        for (int i = 0; i < physicsEntities.size(); i++) {
            PhysicsEntity entity1 = physicsEntities.get(i);
            for (int j = i + 1; j < physicsEntities.size(); j++) {
                PhysicsEntity entity2 = physicsEntities.get(j);

                if (!entity1.hasCollided(entity2))
                    continue;

                PhysicsEntity.ElasticCollisionResults results = entity1.calculateCollisionVelocity(entity2);
                
                /* Limit the position of the spheres */
                entity1.setPosition(results.firstPosition());
                entity2.setPosition(results.secondPosition());
                
                /* Apply the bounce force of the spheres */
                entity1.setVelocity(results.firstVelocity());
                entity2.setVelocity(results.secondVelocity());
                
            }
        }
    }

    @Override
    public void render(float alpha) {
        renderer.clear();

        texture.bind();
        renderer.begin();
        for (PhysicsEntity physicsEntity : physicsEntities) {
            physicsEntity.render(renderer, alpha);
        }
        renderer.end();
    }

    @Override
    public void enter() {
        /* Get width and height of framebuffer */
        try (MemoryStack stack = MemoryStack.stackPush()) {
            long window = GLFW.glfwGetCurrentContext();
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);

            /* Set the game width and height */
            gameWidth = widthBuffer.get();
            gameHeight = heightBuffer.get();
        }

        /* Load texture */
        texture = Texture.loadTexture("./physics-engine/src/main/resources/pong.png");

        physicsEntities.add(new PhysicsEntity(Color.GREEN, texture, (gameWidth - 20f) / 2f, (gameHeight - 20f) / 2f, 10, 1));
        physicsEntities.add(new PhysicsEntity(Color.GREEN, texture, (gameWidth - 20f) / 2.5f, (gameHeight - 20f) / 2f, 10, 1));
        physicsEntities.add(new PhysicsEntity(Color.GREEN, texture, (gameWidth - 20f) / 3.4f, (gameHeight - 20f) / 2f, 10, 1));

        /* Set clear color to gray */
        glClearColor(0.5f, 0.5f, 0.5f, 1f);
    }

    @Override
    public void exit() {
        texture.delete();
    }
    
}
