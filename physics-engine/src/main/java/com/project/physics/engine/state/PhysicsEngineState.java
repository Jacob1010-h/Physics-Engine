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

    private DynamicRenderer renderer;

    private Texture texture;
    private ArrayList<PhysicsEntity> physicsEntities = new ArrayList<>();

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

    public void applyConstraints() {
        for (PhysicsEntity physicsEntity : physicsEntities) {
            physicsEntity.checkBorderCollision(gameWidth, gameHeight);
        }
    }

    public void updateAllEntities(float delta) {
        for (PhysicsEntity physicsEntity : physicsEntities) {
            physicsEntity.update(delta);
        }
    }

    public void solveCollisions() {
        for (int i = 0; i < physicsEntities.size(); i++) {
            PhysicsEntity entity1 = physicsEntities.get(i);
            for (int j = i + 1; j < physicsEntities.size(); j++) {
                PhysicsEntity entity2 = physicsEntities.get(j);

                if (!entity1.hasCollided(entity2))
                    continue;

                PhysicsEntity.ElasticCollisionResults results = entity1.calculateCollisionVelocity(entity2);
                entity1.setVelocity(results.first());
                entity2.setVelocity(results.second());
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
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            long window = GLFW.glfwGetCurrentContext();
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
            width = widthBuffer.get();
            height = heightBuffer.get();
        }

        /* Load texture */
        texture = Texture.loadTexture("./physics-engine/src/main/resources/pong.png");
        
        physicsEntities.add(new PhysicsEntity(Color.GREEN, texture, (width - 20f) / 2f, (height - 20f) / 2f, 10, 1));
        physicsEntities.add(new PhysicsEntity(Color.GREEN, texture, (width - 20f) / 2.2f, (height - 20f) / 2f, 10, 1));
        physicsEntities.add(new PhysicsEntity(Color.GREEN, texture, (width - 20f) / 2.4f, (height - 20f) / 2f, 10, 1));

        gameWidth = width;
        gameHeight = height;

        /* Set clear color to gray */
        glClearColor(0.5f, 0.5f, 0.5f, 1f);
    }

    @Override
    public void exit() {
        texture.delete();
    }
    
}
