package mit.bramtechs.looseunpacker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LooseUnpackerLauncher {

	private final String[] exts = {
			".png",".bmp",".gif",".jpg",".jpeg"
	};
	
	public LooseUnpackerLauncher(String[] args) {

		printArgs(args);
		
		// check if enough args
		switch (args.length) {
		case 0:
			System.err.println("No input file path given.");
			return;
		case 1:
			System.err.println("No output folder given");
			return;
		case 2:
			break;
		default:
			System.err.println("Too many arguments given");
			return;
		}
		
		// check if file exist
		String inputPath = args[0];
		String outputPath = args[1];
		
		File inputFile = new File(inputPath);
		File outputFile = new File(outputPath);
		
		if (!inputFile.exists()) {
			System.err.println("Input file doesn't exist!");
			return;
		}
		
		if (!hasValidExtension(inputFile)) {
			System.err.println("Invalid input format");
		}
		
		outputFile.mkdirs();
		
		BufferedImage image;
		try {
			image = ImageIO.read(inputFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		String name = inputPath.substring(inputPath.lastIndexOf('/')+1);
		String ext = inputPath.substring(inputPath.lastIndexOf('.')+1);
		name = name.substring(0, name.lastIndexOf('.')-1);
		
		SpriteBuilder builder = new SpriteBuilder(outputFile,name,ext);
		ImageUnpacker unpacker = new ImageUnpacker(builder,image);
		unpacker.unpack();
	}
	
	private boolean hasValidExtension(File file) {
		for (String format : exts) {
			if (file.getAbsolutePath().endsWith(format)) {
				return true;
			}
		}
		return false;
	}
	
	private void printArgs(String[] args) {
		System.out.println("--> args");
		for (String arg : args) {
			System.out.println(arg);
		}
		System.out.println("<--");
	}
	
	public static void main(String[] args) {
		new LooseUnpackerLauncher(args);
	}

}
