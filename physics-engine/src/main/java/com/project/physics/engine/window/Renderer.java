package com.project.physics.engine.window;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;

import com.project.physics.engine.components.Component.Color;
import com.project.physics.engine.components.Quad;

public class Renderer {
    private long windowID;
    private final int width;
    private final int height;
    private final String title;

    public Renderer(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public long initWindow() {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Create the window
        windowID = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        return windowID;
    }
    
    public void updateContext() {
        glfwMakeContextCurrent(windowID);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    public void update() {
        glfwPollEvents();
        clear();
        new Quad(100, 100, 200, 200, new Color(0, 255, 255)).render();
        glfwSwapBuffers(windowID);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
