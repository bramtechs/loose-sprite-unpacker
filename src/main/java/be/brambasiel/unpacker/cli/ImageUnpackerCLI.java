package be.brambasiel.unpacker.cli;

import be.brambasiel.unpacker.ImageUnpacker;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ImageUnpackerCLI {
    private static final Logger logger = Logger.getLogger(ImageUnpackerCLI.class.getName());

    private void checkArgsOrThrow(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No arguments given");
        }
        if (args.length == 1) {
            throw new IllegalArgumentException("No output folder given");
        }
        if (args.length > 2) {
            throw new IllegalArgumentException("Too many arguments given");
        }
    }

    public ImageUnpackerCLI(String[] args) {
        checkArgsOrThrow(args);

        File imageFile = new File(args[0]);
        File outputFolder = new File(args[1]);

        ImageUnpacker.run(imageFile, outputFolder);
    }
}
