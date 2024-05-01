package com.project.physics.engine;

import com.project.physics.engine.window.Window;

public class Main {
    
    public static void main(String[] args) {
        Window window = new Window(640, 480, "Hello World!", true);
        window.run();
    }
}
