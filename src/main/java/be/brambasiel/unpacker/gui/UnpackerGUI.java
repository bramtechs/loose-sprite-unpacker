package be.brambasiel.unpacker.gui;

import be.brambasiel.unpacker.ImageUnpacker;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class UnpackerGUI implements UnpackerActions {

    private static final Logger logger = Logger.getLogger(UnpackerGUI.class.getName());

    public UnpackerGUI() {
        new UnpackerJFrame(this);
    }

    @Override
    public void unpack(File imageFile, File outputFolder) {

        ImageUnpacker.runAsync(imageFile, outputFolder);
    }

    @Override
    public void openExplorer(File folder) {
        if (Desktop.isDesktopSupported()){
            try {
                Desktop.getDesktop().open(folder);
            } catch (IOException e) {
                logger.severe("Failed to open explorer window");
            }
        }else{
            logger.warning("Desktop not supported");
        }
    }

    @Override
    public File selectInputFile() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public File selectOutputFile() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
