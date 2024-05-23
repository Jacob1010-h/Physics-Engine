package com.project.physics.engine.state;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import static org.lwjgl.opengl.GL11.glClearColor;
import org.lwjgl.system.MemoryStack;

import com.project.physics.engine.game.CircleConstraint;
import com.project.physics.engine.game.PhysicsEntity;
import com.project.physics.engine.graphic.DynamicCamera;
import com.project.physics.engine.graphic.shader.Color;
import com.project.physics.engine.graphic.shader.Texture;
import com.project.physics.engine.math.Vector2f;

/**
 *
 * @author jacob
 */
public class PhysicsEngineState implements State {

    private final DynamicCamera renderer;
    private Texture texture;
    private final ArrayList<PhysicsEntity> physicsEntities = new ArrayList<>();
    private int gameWidth;
    private int gameHeight;
    private GLFWMouseButtonCallback mouseCallback;
    private final float deltaStep = 8f;

    public PhysicsEngineState(DynamicCamera renderer) {
        this.renderer = renderer;
    }
    
    @Override
    public void input() {
        long window = GLFW.glfwGetCurrentContext();

        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, x, y);
        float mouseX = (float) x.get();
        float mouseY = (float) y.get();

        /* Create binding listener for a single button click */
        glfwSetMouseButtonCallback(window, mouseCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                switch (action) {
                    case GLFW_PRESS -> {
                        makePhysicsEntityAtMouse(mouseX, mouseY);
                    }
                }
            }
        });

        /* Checks every frame if [SPACE] is pressed */
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            makePhysicsEntityAtMouse(mouseX, mouseY);
        }
    }

    // Using this method too much within a short amount of time will result in the texture never being spawned
    private void makePhysicsEntityAtMouse(float mouseX, float mouseY) {
        physicsEntities
                .add(new PhysicsEntity(Color.GREEN, texture, mouseX - 10, -mouseY + gameHeight - 10, 10,
                        new CircleConstraint(new Vector2f(gameWidth / 2f, gameHeight / 2f), 200f)));
    }
    
    private void updateWindowDimensions() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            long window = GLFW.glfwGetCurrentContext();
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);

            /* Set the game width and height */
            gameWidth = widthBuffer.get();
            gameHeight = heightBuffer.get();
        }
    }

    @Override
    public void update(float delta, boolean hasResized, int windowWidth, int windowHeight) {
        if (hasResized) {
            updateWindowDimensions();
        }

        /* Physics Update Step */
        for (int i = 0; i < (int) deltaStep; i++) {
            for (int j = 0; j < physicsEntities.size(); j++) {
                PhysicsEntity entity1 = physicsEntities.get(j);

                /* Check the boarder for any collisions */
                entity1.checkBorderCollision(windowWidth, windowHeight);

                /* Check the entities in the area for any collisions with each other */
                for (int k = j + 1; k < physicsEntities.size(); k++) {
                    PhysicsEntity entity2 = physicsEntities.get(k);
                    if (!entity1.hasCollided(entity2))
                        continue;
                    PhysicsEntity.PhysicalCollisionResults results = entity1.calculateCollisionVelocity(entity2);
                    /* Limit the position of the spheres */
                    entity1.setPosition(results.first());
                    entity2.setPosition(results.second());
                }

                /*Update the entities to complete the loop */
                entity1.update(delta / deltaStep);
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
        updateWindowDimensions();

        /* Load texture */
        texture = Texture.loadTexture("./physics-engine/src/main/resources/pong.png");

        /* Set clear color to gray */
        glClearColor(0.5f, 0.5f, 0.5f, 1f);
    }

    @Override
    public void exit() {
        mouseCallback.free();
        texture.delete();
    }
    
}
