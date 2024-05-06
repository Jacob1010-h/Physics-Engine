/*
 * The MIT License (MIT)
 *
 * Copyright © 2015, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.project.physics.engine.game;

import com.project.physics.engine.math.Vector2f;

/**
 * This class represents an axis-aligned bounding box.
 *
 * @author Heiko Brumme
 */
public class AxisBoundingBox {

    public Vector2f min, max, center;
    
    public AxisBoundingBox(float x, float y, float width, float height) {
        min = new Vector2f(x, y);
        max = new Vector2f(
                x + width,
                y + height);
        center = min.subtract(max).abs();
    }
    public AxisBoundingBox(Entity entity) {
        this(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());
    }
    

    /**
     * Checks if this AxisBoundingBox intersects another AxisBoundingBox.
     *
     * @param other The other AxisBoundingBox
     *
     * @return true if a collision was detected.
     */
    public boolean intersects(AxisBoundingBox other) {
        if (this.max.x < other.min.x) {
            return false;
        }

        if (this.max.y < other.min.y) {
            return false;
        }

        if (this.min.x > other.max.x) {
            return false;
        }

        if (this.min.y > other.max.y) {
            return false;
        }

        // All tests failed, we have a intersection
        return true;
    }

}
