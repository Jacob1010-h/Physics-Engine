

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.project.physics.engine.game.AxisBoundingBox;

public class AxisBoundingBoxTest {

    @Test
    public void testIntersects() {
        // Create two AxisBoundingBox objects for testing
        AxisBoundingBox box1 = new AxisBoundingBox(0f, 0f, 2f, 2f);
        AxisBoundingBox box2 = new AxisBoundingBox(3f, 3f, 2f, 2f);

        // Test when the boxes do not intersect
        assertFalse(box1.intersects(box2));

        // Test when the boxes intersect
        box2 = new AxisBoundingBox(1f, 1f, 2f, 2f);
        assertTrue(box1.intersects(box2));

        // Test when one box is completely inside the other
        box2 = new AxisBoundingBox(0f, 0f, 1f, 1f);
        assertTrue(box1.intersects(box2));

        // Test when the boxes share an edge
        box2 = new AxisBoundingBox(2f, 0f, 2f, 2f);
        assertTrue(box1.intersects(box2));
    }
}