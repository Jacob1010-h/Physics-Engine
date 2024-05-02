package com.project.physics.game;

import org.pmw.tinylog.Logger;

import com.project.physics.engine.AppLogic;
import com.project.physics.engine.Engine;
import com.project.physics.engine.Render;
import com.project.physics.engine.Scene;
import com.project.physics.engine.Window;

public class Main implements AppLogic {
    
    public static void main(String[] args) {
        Main main = new Main();
        Engine gameEng = new Engine("Hello World!", new Window.WindowOptions(), main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        Logger.info("The window has been closed/quit");
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        Logger.info("The window has been initialized");
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        // Nothing to be done yet
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        // Nothing to be done yet
    }
}
