package com.project.physics.engine;

import java.util.concurrent.TimeUnit;

import com.project.physics.engine.Window.WindowOptions;

public class Engine {

    public static final int TARGET_UPS = 30;
    private final AppLogic appLogic;
    private final Window window;
    private final Render render;
    private boolean running;
    private final Scene scene;
    private final WindowOptions windowOptions;

    public Engine(String windowTitle, Window.WindowOptions windowOptions, AppLogic appLogic) {
        window = new Window(windowTitle, windowOptions, () -> {
            resize();
            return null;
        });
        this.windowOptions = windowOptions;
        this.appLogic = appLogic;
        render = new Render();
        scene = new Scene();
        appLogic.init(window, scene, render);
        running = true;
    }

    private void cleanup() {
        appLogic.cleanup();
        render.cleanup();
        scene.cleanup();
        window.cleanup();
    }

    private void resize() {
        // Nothing to be done yet
    }

    private void run() {
        long initialTime = System.currentTimeMillis();
        
        float updateTimeMillis = TimeUnit.MILLISECONDS.convert(windowOptions.updatesPerSec, TimeUnit.SECONDS);
        float frameRateMillis = windowOptions.fps > 0 ? TimeUnit.MILLISECONDS.convert(windowOptions.fps, TimeUnit.SECONDS) : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / updateTimeMillis;
            deltaFps += (now - initialTime) / frameRateMillis;

            if (windowOptions.fps <= 0 || deltaFps >= 1) {
                appLogic.input(window, scene, now - initialTime);
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                appLogic.update(window, scene, diffTimeMillis);
                updateTime = now;
                deltaUpdate--;
            }

            if (windowOptions.fps <= 0 || deltaFps >= 1) {
                render.render(window, scene);
                deltaFps--;
                window.update();
            }
            initialTime = now;
        }

        cleanup();
    }

    public void start() {
        running = true;
        run();
    }

    public void stop() {
        running = false;
    }

}