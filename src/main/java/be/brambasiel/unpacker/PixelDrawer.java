package be.brambasiel.unpacker;

import java.awt.*;

public interface PixelDrawer {
    void drawPixel(Graphics2D graph, int x, int y, Color color);
}
