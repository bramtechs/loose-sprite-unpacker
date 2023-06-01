package be.brambasiel.unpacker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageLoader {
    public static BufferedImage loadImage(File imageFile){
        if (!imageFile.exists()) {
            throw new IllegalArgumentException("Image file does not exist");
        }
        return readImage(imageFile);
    }

    private static BufferedImage readImage(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                throw new NullPointerException("Image could not be read");
            }
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to read image!");
        }
    }
}
