package be.brambasiel.unpacker.collections;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

public class PixelMap extends HashMap<Point, Color> {

    public Rectangle bounds() {
        int largestX = Integer.MIN_VALUE;
        int largestY = Integer.MIN_VALUE;
        int smallestX = Integer.MAX_VALUE;
        int smallestY = Integer.MAX_VALUE;
        for (Point pos : this.keySet()) {
            if (pos.x > largestX) {
                largestX = pos.x;
            }
            if (pos.x < smallestX) {
                smallestX = pos.x;
            }
            if (pos.y > largestY) {
                largestY = pos.y;
            }
            if (pos.y < smallestY) {
                smallestY = pos.y;
            }
        }
        return new Rectangle(smallestX, smallestY, largestX - smallestX + 1, largestY - smallestY + 1);
    }
}
