package com.project.physics.engine.state;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.glClearColor;
import org.lwjgl.system.MemoryStack;

import com.project.physics.engine.game.PhysicsEntity;
import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.graphic.window.Renderer;

/**
 *
 * @author jacob
 */
public class PhysicsEngineState implements State {

    private Renderer renderer;

    private Texture texture;
    private PhysicsEntity triangle;

    private int gameWidth;
    private int gameHeight;

    public PhysicsEngineState(Renderer renderer) {
        this.renderer = renderer;
    
    }
    
    @Override
    public void input() {
        // There is no input for this yet :))
    }

    @Override
    public void update(float delta) {
        triangle.update(delta);

        triangle.checkBorderCollision(gameWidth, gameHeight);
    }

    @Override
    public void render(float alpha) {
        renderer.clear();

        texture.bind();
        renderer.begin();
        triangle.render(renderer, alpha);
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
        float speed = 250f;
        
        triangle = new PhysicsEntity(Color.GREEN, texture, (width - 20) / 2f, (height - 20) / 2f, speed * 1.5f);


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
