package be.brambasiel.unpacker;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import be.brambasiel.unpacker.collections.PixelMap;

import javax.imageio.ImageIO;

public class ImageUnpacker {
    private static final Logger logger = Logger.getLogger(ImageUnpacker.class.getName());
    private final ImageWriter writer;
    private BufferedImage image;
    private boolean[][] traversed;

    private ImageUnpacker(File imageFile, File outputFolder) {
        this.image = ImageLoader.loadImage(imageFile);
        this.writer = new ImageWriter(imageFile, outputFolder);
    }

    public void unpack() {
        traversed = new boolean[image.getWidth()][image.getHeight()];

        // check each pixel that isn't transparent
        int i = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                if (traversed[x][y]) // don't go over already checked pixels
                    continue;

                Color col = getPixel(x, y);
                if (isOpaque(col)) {
                    // 'magicwand' the surrounding shape
                    PixelMap pixels = new PixelMap();
                    explore(pixels, x, y);
                    logger.fine(() -> String.format("Found shape with %d pixels", pixels.size()));

                    writer.setPixels(pixels);
                    writer.export(i);

                    i++;
                } else {
                    traversed[x][y] = true;
                }
            }
        }
    }

    private boolean isOpaque(Color c) {
        return c.getAlpha() > 0;
    }

    private void explore(HashMap<Point, Color> pixels, int x, int y) { // recursion,recursion,recursion,recursion, ...
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return;
        }
        if (!traversed[x][y]) {
            traversed[x][y] = true;
            Color col = getPixel(x, y);
            if (isOpaque(col)) {
                pixels.put(new Point(x, y), col);

                explore(pixels, x - 1, y - 1);
                explore(pixels, x + 0, y - 1);
                explore(pixels, x + 1, y - 1);

                explore(pixels, x - 1, y + 0);
                explore(pixels, x + 1, y + 0);

                explore(pixels, x - 1, y + 1);
                explore(pixels, x + 0, y + 1);
                explore(pixels, x + 1, y + 1);
            }
        }
    }

    private Color getPixel(int x, int y) {
        int colorInt = image.getRGB(x, y);
        return new Color(colorInt, true);
    }

    public static void runAsync(File imageFile, File outputFolder) {
        runAsync(imageFile, outputFolder, () -> {});
    }

    public static void runAsync(File imageFile, File outputFolder, Runnable callback) {
        new Thread(() -> {
            run(imageFile, outputFolder);
            callback.run();
        }).start();
    }

    public static void run(File imageFile, File outputFile) {
        try {
            ImageUnpacker unpacker = new ImageUnpacker(imageFile, outputFile);
            unpacker.unpack();
        } catch (Exception e) {
            logger.severe("Failed to unpack image");
            logger.severe(e.toString());
            e.printStackTrace();
        }
    }
}
