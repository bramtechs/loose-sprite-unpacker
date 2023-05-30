package be.brambasiel.unpacker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import be.brambasiel.unpacker.collections.PixelMap;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nullable;

public class ImageWriter {

    private static final Logger logger = Logger.getLogger(ImageWriter.class.getName());

    private final File imageFile;
    private final File outputFolder;
    @Nullable
    private BufferedImage image;

    public ImageWriter(File imageFile, File outputFolder) {
        this.outputFolder = outputFolder;
        this.imageFile = imageFile;
    }

    public void setPixels(PixelMap pixels) {
        Rectangle bounds = pixels.bounds();
        image = createGraphics(bounds, pixels, (g, x, y, color) -> {
			// HACK: draw a line instead of a pixel to avoid antialiasing
            g.setColor(color);
            g.drawLine(x, y, x, y);
        });
    }

    public void export(int index) {
        if (image == null) {
            throw new NullPointerException("No pixels set in ImageWriter");
        }

        String name = FilenameUtils.getBaseName(imageFile.getName());
        String extension = FilenameUtils.getExtension(imageFile.getName());
        FilenameUtils.concat(outputFolder.getAbsolutePath(), name + "_" + index + "." + extension);

        File file = Paths.get(outputFolder.getAbsolutePath())
                .resolve(name + "_" + index + "." + extension).toFile();

        try {
			outputFolder.mkdirs();
            ImageIO.write(image, extension, file);
        } catch (Exception e) {
            logger.severe(() -> "Failed to write image to file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

	private BufferedImage createGraphics(Rectangle bounds, PixelMap pixels, PixelDrawer drawer) {
		BufferedImage img = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		for (Entry<Point, Color> entry : pixels.entrySet()) {
			drawer.drawPixel(graphics, entry.getKey().x - bounds.x, entry.getKey().y - bounds.y, entry.getValue());
		}
		graphics.dispose();
		img.flush();
		return img;
	}
}
