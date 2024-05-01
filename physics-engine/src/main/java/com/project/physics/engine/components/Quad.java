package com.project.physics.engine.components;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Quad extends Component {

    private final Color color;

    public Quad(int x, int y, float width, float height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    @Override
    public void render() {
        glColor3f(color.r(), color.g(), color.b());

        glBegin(GL_QUADS);

        glVertex2f(-x, y);
        glVertex2f(width, y);
        glVertex2f(width, -height);
        glVertex2f(-x, -height);

        glEnd();
    }

}
