package com.project.physics.engine.window;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import com.project.physics.engine.components.Component.Color;
import com.project.physics.engine.components.Quad;

public class Renderer {
    private final long windowID;

    public Renderer(long windowID) {
        this.windowID = windowID;
    }

    public static void reRender(long windowID) {
        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        new Quad(100, 100, 200, 200, new Color(0, 255, 255)).render();
        glfwSwapBuffers(windowID);
    }

    public void reRender() {
        reRender(windowID);
    }
}
