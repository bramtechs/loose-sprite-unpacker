package mit.bramtechs.looseunpacker;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ImageUnpacker {

	private final BufferedImage image;
	private final SpriteBuilder builder;
	private boolean[][] traversed;
	
	public ImageUnpacker(SpriteBuilder builder, BufferedImage image) {
		this.image = image;
		this.builder = builder;
	}

	public void unpack() {

		traversed = new boolean[image.getWidth()][image.getHeight()];

		// check each pixel that isn't transparent
		int i = 0;
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {

				if (traversed[x][y] == true) // don't go over already checked pixels
					continue;
				
				Color col = getPixel(x,y);
				if (!isAlpha(col)) {
					// 'magicwand' the surrounding shape
					HashMap<Point, Color> points = new HashMap<Point, Color>();
					explore(points, x, y);
					System.out.println("Found shape with " + points.size() + " pixels");
					BufferedImage image = builder.construct(points);
					builder.export(image,i);
					i++;
				}else {
					traversed[x][y] = true;
				}
			}
		}
	}

	private boolean isAlpha(Color c) {
		return c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 0 && c.getAlpha() == 0;
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
				
				explore(pixels, x-1, y-1);
				explore(pixels, x+0, y-1);
				explore(pixels, x+1, y-1);
				
				explore(pixels, x-1, y+0);
				explore(pixels, x+1, y+0);
				
				explore(pixels, x-1, y+1);
				explore(pixels, x+0, y+1);
				explore(pixels, x+1, y+1);
			}
		}
	}

	private Color getPixel(int x, int y) {
		int colorInt = image.getRGB(x, y);
		return new Color(colorInt, true);
	}
}
