package com.project.physics.engine.game;

import com.project.physics.engine.window.Window;

public interface GameLogic {
    void init() throws Exception;

    void input(Window window);

    void update(float interval);

    void render(Window window);
}
