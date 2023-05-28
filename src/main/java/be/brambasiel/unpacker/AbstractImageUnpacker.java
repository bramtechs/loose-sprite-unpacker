package be.brambasiel.unpacker;

import java.io.File;
import java.util.logging.Logger;

public abstract class AbstractImageUnpacker {
    private static final Logger logger = Logger.getAnonymousLogger(ImageUnpacker.class.getName());

    protected void process(File imageFile, File outputFolder) {
        try {
            ImageUnpacker unpacker = new ImageUnpacker(imageFile, outputFolder);
            unpacker.unpack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
