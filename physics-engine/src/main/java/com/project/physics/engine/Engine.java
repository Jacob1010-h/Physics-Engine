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
        long initialTime_ms = System.currentTimeMillis();
        
        float updateTime_ms = TimeUnit.MILLISECONDS.convert(windowOptions.updatesPerSec, TimeUnit.SECONDS);
        float frameRate_ms = windowOptions.fps > 0 ? TimeUnit.MILLISECONDS.convert(windowOptions.fps, TimeUnit.SECONDS) : 0;
        float deltaUpdate_ms = 0;
        float deltaFramesPer_ms = 0;

        long updateTime = initialTime_ms;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate_ms += (now - initialTime_ms) / updateTime_ms;
            deltaFramesPer_ms += (now - initialTime_ms) / frameRate_ms;

            if (windowOptions.fps <= 0 || deltaFramesPer_ms >= 1) {
                appLogic.input(window, scene, now - initialTime_ms);
            }

            if (deltaUpdate_ms >= 1) {
                long diffTime_ms = now - updateTime;
                appLogic.update(window, scene, diffTime_ms);
                updateTime = now;
                deltaUpdate_ms--;
            }

            if (windowOptions.fps <= 0 || deltaFramesPer_ms >= 1) {
                render.render(window, scene);
                deltaFramesPer_ms--;
                window.update();
            }
            initialTime_ms = now;
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