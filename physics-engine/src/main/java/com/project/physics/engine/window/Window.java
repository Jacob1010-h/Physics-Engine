package com.project.physics.engine.window;

import org.lwjgl.Version;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWErrorCallback;

public class Window {

    private long windowID;
    
    private final int width;
    private final int height;
    private final String title;

    private final boolean vSync;
    
    private Renderer renderer;

    public Window(int width, int height, String title, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;

        this.vSync = vSync;

        run();
    }

    private void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init(width, height, title);
        loop();

        glfwTerminate();
    }

    private void init(int width, int height, String title) {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        renderer = new Renderer(width, height, title);
        windowID = renderer.initWindow();
        
        createExitBinding();

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowID);

        // Enable v-sync
        if (vSync)
            glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowID);
    }

    private void createExitBinding() {
        // Setup a key callback. It will be called every time a key is pressed, repeated
        // or released.
        glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });
    }

    private void loop() {
        renderer.updateContext();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(windowID)) {
            renderer.update();
        }
    }
    
    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowID, keyCode) == GLFW_PRESS;
    }
}
