package com.project.physics.engine.window;

import org.lwjgl.Version;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private long windowID;
    
    private final int width;
    private final int height;
    private final String title;

    private final boolean vSync;

    public Window(int width, int height, String title, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;

        this.vSync = vSync;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        try {
            init(width, height, title);
        } catch (Exception e) {
            System.err.println("failed to initilize window");
        }

        loop();

        glfwTerminate();
    }

    private void init(int width, int height, String title) throws Exception {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        windowID = initWindow(width, height, title);

        createExitBinding();

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowID);

        // Enable v-sync
        if (vSync)
            glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowID);
    }
    
    private long initWindow(int width, int height, String title) throws Exception {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Create the window
        windowID = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        return windowID;
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
        updateContext();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(windowID)) {
            // TODO: this is currently just updating the window because of the lack of a game loop at the moment
            updateWindow();
        }
    }

    private void updateWindow() {
        Renderer.reRender(windowID);
    }
    
    private void updateContext() {
        glfwMakeContextCurrent(windowID);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }
    
    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowID, keyCode) == GLFW_PRESS;
    }
}
