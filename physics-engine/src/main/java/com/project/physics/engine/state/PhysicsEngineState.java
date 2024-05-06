package com.project.physics.engine.state;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.glClearColor;
import org.lwjgl.system.MemoryStack;

import com.project.physics.engine.game.PhysicsEntity;
import com.project.physics.engine.game.PhysicsEntity.ElasticCollisionResults;
import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.graphic.window.DynamicRenderer;
import com.project.physics.engine.math.Vector2f;

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
        for (PhysicsEntity physicsEntity : physicsEntities) {
            physicsEntity.update(delta);
            physicsEntity.checkBorderCollision(gameWidth, gameHeight);
            for (PhysicsEntity physicsEntity2 : physicsEntities) {
                // check if the checked physics entity is equal to the current entity being looped
                if (physicsEntity.getPosition().equals(physicsEntity2.getPosition()))
                    continue;
                
                if (physicsEntity.hasCollided(physicsEntity2)) {
                    ElasticCollisionResults results = physicsEntity.calculateCollisionVelocity(physicsEntity2);
                    System.out.println(results.toString());
                    physicsEntity.setVelocity(results.first());
                    physicsEntity2.setVelocity(results.second());
                }
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

        /* Initialize game objects */
        float speed = 9.5f;
        
        for (int i = 0; i < 5; i++) {
            float x = (float) (Math.random() - 0.5f) * 30f;
            float y = (float) (Math.random() - 0.5f) * 30f;
            physicsEntities.add(new PhysicsEntity(Color.GREEN, texture, (width - 20) / 2f, (height - 20) / 2f, speed * 1.5f, new Vector2f(x, y), 1));
        }

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
