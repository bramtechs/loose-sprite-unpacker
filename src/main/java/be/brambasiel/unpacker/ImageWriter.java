package be.brambasiel.unpacker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import be.brambasiel.unpacker.collections.PixelMap;

public class ImageWriter {

	private static Logger logger = Logger.getAnonymousLogger(ImageWriter.class.getName());

	private final File outputFolder;
	private final String name;
	private final String ext;

	private BufferedImage image;

	public ImageWriter(File outputFolder, String name, String ext) {
		this.outputFolder = outputFolder;
		this.name = name;
		this.ext = ext;
	}

	public void setPixels(PixelMap pixels) {
		Rectangle bounds = pixels.bounds();
		image = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = image.createGraphics();
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		for (Entry<Point, Color> entry : pixels.entrySet()) {
			Point p = entry.getKey();
			Color col = entry.getValue();
			gr.setColor(col);
			int x = p.x - bounds.x;
			int y = p.y - bounds.y;
			if (x < 0 || x >= bounds.width || y < 0 || y >= bounds.height) {
				logger.warning("Pixel drawn outside of texture");
			}

			gr.drawLine(x, y, x, y);
		}
		gr.dispose();
		image.flush();
	}

	public void export(int index) {
		if (image == null) {
			throw new NullPointerException("No pixels set in ImageWriter");
		}

		File file = Paths.get(outputFolder.getAbsolutePath())
				.resolve(name + "_" + index + "." + ext).toFile();
		try {
			ImageIO.write(image, ext, file);
		} catch (IOException e) {
			logger.severe(() -> "Failed to write image to file: " + file.getAbsolutePath());
			e.printStackTrace();
		}
	}
}
