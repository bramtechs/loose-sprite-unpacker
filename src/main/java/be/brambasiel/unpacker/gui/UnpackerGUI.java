package be.brambasiel.unpacker.gui;

import be.brambasiel.unpacker.ImageUnpacker;
import be.brambasiel.unpacker.cli.ImageUnpackerCLI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Button;
import java.awt.Desktop;
import java.awt.TextArea;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.io.Serial;
import java.nio.file.Path;
import java.util.logging.Logger;

public class UnpackerGUI implements UnpackerActions {

    private static final Logger logger = Logger.getLogger(UnpackerJFrame.class.getName());
    private final UnpackerJFrame frame;

    private Path inputFile;
    private Path outputFolder;

    public UnpackerGUI() {
        frame = new UnpackerJFrame();
    }

    @Override
    public void unpack(File imageFile, File outputFolder) {
        try {
            ImageUnpacker unpacker = new ImageUnpacker(imageFile, outputFolder);
            unpacker.unpack();
        } catch (Exception e) {
            logger.severe(e.toString());
            e.printStackTrace();
        }
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
    public void selectInputFile() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void selectOutputFile() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
