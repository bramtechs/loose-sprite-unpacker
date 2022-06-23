package mit.bramtechs.looseunpacker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class SpriteBuilder {
	
	private final File outputFolder;
	private final String name;
	private final String ext;
	
	public SpriteBuilder(File outputFolder,String name, String ext) {
		this.outputFolder = outputFolder;
		this.name = name;
		this.ext = ext;
	}
	
	private Rectangle calculateBounds(HashMap<Point, Color> pixels) {
		int largestX = 0;
		int largestY = 0;
		int smallestX = Integer.MAX_VALUE;
		int smallestY = Integer.MAX_VALUE;
		for (Point pos : pixels.keySet()) {
			if (pos.x > largestX) {
				largestX = pos.x;
			}
			if (pos.y > largestY) {
				largestY = pos.y;
			}
			if (pos.x < smallestX) {
				smallestX = pos.x;
			}
			if (pos.y < smallestY) {
				smallestY = pos.y;
			}
		}
		return new Rectangle(smallestX,smallestY,largestX-smallestX+1,largestY-smallestY+1);
	}
	
	public BufferedImage construct(HashMap<Point, Color> pixels) {
		Rectangle bounds = calculateBounds(pixels);
		BufferedImage img = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = img.createGraphics();
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		for (Entry<Point,Color> entry : pixels.entrySet()) {
			Point p = entry.getKey();
			Color col = entry.getValue();
			gr.setColor(col);
			int x = p.x-bounds.x;
			int y = p.y-bounds.y;
			if (x < 0 || x >= bounds.width || y < 0 || y >= bounds.height) {
				System.err.println("Pixel drawn outside of texture");
			}
			
			gr.drawLine(x, y, x, y);
		}
		gr.dispose();
		img.flush();
		return img;
	}
	
	public void export(BufferedImage image, int index) {
		String path = outputFolder.getAbsolutePath()+"/"+name+"_"+Integer.toString(index)+"."+ext;
		File file = new File(path);
		try {
			ImageIO.write(image,ext,file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
