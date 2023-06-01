package be.brambasiel.unpacker;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Logger;

import be.brambasiel.unpacker.collections.PixelMap;

public class ImageUnpacker {
    private static final Logger logger = Logger.getLogger(ImageUnpacker.class.getName());
    private final ImageWriter writer;
    private BufferedImage image;
    private Set<Point> traversed;

    private ImageUnpacker(File imageFile, File outputFolder) {
        this.image = ImageLoader.loadImage(imageFile);
        this.writer = new ImageWriter(imageFile, outputFolder);
    }

    public void unpack() {
        traversed = new HashSet<>();

        // check each pixel that isn't transparent
        int i = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Point point = new Point(x, y);
                if (isValid(point)) {
                    // 'magicwand' the surrounding shape
                    PixelMap pixels = collect(point);
                    logger.fine(() -> String.format("Found shape with %d pixels", pixels.size()));

                    writer.setPixels(pixels);
                    writer.export(i);
                    i++;
                } else {
                    traversed.add(point);
                }
            }
        }
    }

    private boolean isOpaque(Color c) {
        return c.getAlpha() > 25;
    }

    private void register(Point point, PixelMap map) {
        Color col = getPixel(point.x, point.y);
        if (!traversed.add(point)) {
            throw new IllegalStateException(String.format("Pixel %s was already visited", point));
        }
        if (map.put(point, col) != null) {
            throw new IllegalStateException(String.format("Pixel %s already in map", point));
        }
    }

    private boolean isValid(Point point) {
        Color col = getPixel(point.x, point.y);
        return isOpaque(col) && !traversed.contains(point);
    }

    private PixelMap collect(Point point) {
        PixelMap pixels = new PixelMap();

        final Point[] neighbors = new Point[]{
                new Point(-1, -1),
                new Point(0, -1),
                new Point(1, -1),
                new Point(-1, 0),
                new Point(1, 0),
                new Point(-1, 1),
                new Point(0, 1),
                new Point(1, 1),
        };

        LinkedList<Point> points = new LinkedList<>();
        points.push(point);
        while (!points.isEmpty()) {
            Point p = points.pop();
            if (isValid(p)) {
                register(p, pixels);
            }

            for (Point n : neighbors) {
                Point neighbor = new Point(p.x + n.x, p.y + n.y);
                if (isValid(neighbor)) {
                    points.push(neighbor);
                }
            }
        }

        return pixels;
    }

    private Color getPixel(int x, int y) {
        int colorInt = image.getRGB(x, y);
        return new Color(colorInt, true);
    }

    public static void runAsync(File imageFile, File outputFolder) {
        runAsync(imageFile, outputFolder, () -> {
        });
    }

    public static void runAsync(File imageFile, File outputFolder, Runnable callback) {
        new Thread(() -> {
            run(imageFile, outputFolder);
            callback.run();
        }).start();
    }

    public static void run(File imageFile, File outputFolder) {
        try {
            ImageUnpacker unpacker = new ImageUnpacker(imageFile, outputFolder);
            unpacker.unpack();
        } catch (Exception e) {
            logger.severe("Failed to unpack image");
            logger.severe(e.toString());
            e.printStackTrace();
        }
    }
}
