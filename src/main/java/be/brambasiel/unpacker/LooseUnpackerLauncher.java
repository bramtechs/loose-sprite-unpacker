package be.brambasiel.unpacker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class LooseUnpackerLauncher {
	private static final Logger logger = Logger.getAnonymousLogger(LooseUnpackerLauncher.class.getName());

	private final String[] exts = {
			".png",".bmp",".gif",".jpg",".jpeg"
	};
	
	public LooseUnpackerLauncher(String[] args) throws IOException {
		// check if enough args
		switch (args.length) {
		case 0:
			// open the gui instead
			new LooseUnpackerGUI();
			return;
		case 1:
			logger.severe("No output folder given");
			return;
		case 2:
			break;
		default:
			logger.severe("Too many arguments given");
			return;
		}
		
		// check if file exist
		String inputPath = args[0];
		String outputPath = args[1];
		
		File inputFile = new File(inputPath);
		File outputFile = new File(outputPath);
		
		if (!inputFile.exists()) {
			logger.severe("Input file doesn't exist!");
			return;
		}
		
		if (!hasValidExtension(inputFile)) {
			logger.severe("Invalid input format");
		}
		
		outputFile.mkdirs();
		
		BufferedImage image;
		try {
			image = ImageIO.read(inputFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		
		String name = inputPath.substring(inputPath.lastIndexOf('/')+1);
		String ext = inputPath.substring(inputPath.lastIndexOf('.')+1);
		name = name.substring(0, name.lastIndexOf('.')-1);
		
		ImageWriter builder = new ImageWriter(outputFile,name,ext);
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
		logger.fine("--> args");
		for (String arg : args) {
			logger.fine(arg);
		}
		logger.fine("<--");
	}
	
	public static void main(String[] args) throws IOException {
		new LooseUnpackerLauncher(args);
	}

}
