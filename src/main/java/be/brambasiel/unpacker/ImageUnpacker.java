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

    private final File imageFile;
    private final ImageWriter writer;
    private BufferedImage image;
    private boolean[][] traversed;

    private ImageUnpacker(File imageFile, File outputFolder) {
        if (!imageFile.exists()) {
            throw new IllegalArgumentException("Image file does not exist");
        }
        if (!outputFolder.isDirectory()) {
            throw new IllegalArgumentException("Output folder is not a directory");
        }
        this.imageFile = imageFile;
        this.writer = new ImageWriter(imageFile, outputFolder);
    }

    private BufferedImage readImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                throw new NullPointerException("Image could not be read");
            }
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to read image!");
        }
    }

    public void unpack() {
        image = readImage(writer.getImageFile());
        traversed = new boolean[image.getWidth()][image.getHeight()];

        // check each pixel that isn't transparent
        int i = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                if (traversed[x][y]) // don't go over already checked pixels
                    continue;

                Color col = getPixel(x, y);
                if (!isAlpha(col)) {
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

    private boolean isAlpha(Color c) {
        return c.getAlpha() == 0;
    }

    private void explore(HashMap<Point, Color> pixels, int x, int y) { // recursion,recursion,recursion,recursion, ...
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return;
        }
        if (traversed[x][y] == false) {
            traversed[x][y] = true;
            Color col = getPixel(x, y);
            if (!isAlpha(col)) {
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
        new Thread(() -> {
            run(imageFile, outputFolder);
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
