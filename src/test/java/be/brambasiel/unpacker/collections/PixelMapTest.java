package be.brambasiel.unpacker.collections;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Color;

class PixelMapTest {

    @Test
    void testBounds() {
        PixelMap pixels = new PixelMap();
        pixels.put(new Point(0, -3), Color.BLACK);
        pixels.put(new Point(-10, 0), Color.BLACK);
        pixels.put(new Point(0, 1), Color.BLACK);
        pixels.put(new Point(3, 5), Color.BLACK);
        pixels.put(new Point(6, 20), Color.BLACK);

        Rectangle expected = new Rectangle(-10, -3, 17, 24);
        assertEquals(expected, pixels.bounds());
    }
}
