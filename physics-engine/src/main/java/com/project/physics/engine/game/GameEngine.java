package com.project.physics.engine.game;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import com.project.physics.engine.window.Window;

public class GameEngine implements Runnable {

    private Window window;
    private GameLogic gameLogic;

    public void init() {
        // TODO: write this!!
    }

    public void gameLoop() {
        // TODO: write this!!
    }

    public void start() {
        gameLoop.start();
    }

    public double getTime() {
        return glfwGetTime();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e) {
        }
    }

    private final Thread gameLoop;

    public GameEngine(int width, int height, String windowTitle, GameLogic gameLogic) {
        gameLoop = new Thread(this, "GAME_LOOP_THREAD");
        this.window = new Window(640, 480, "Hello World!", true);
        window.run();
        this.gameLogic = gameLogic;
    }
}
