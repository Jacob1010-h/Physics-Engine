package com.project.physics.engine.game;

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
        gameLoop = new Thread();
        this.window = new Window(640, 480, "Hello World!", true);
        this.gameLogic = gameLogic;
    }
}
