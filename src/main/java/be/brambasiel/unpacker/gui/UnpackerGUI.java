package be.brambasiel.unpacker.gui;

import be.brambasiel.unpacker.ImageUnpacker;
import be.brambasiel.unpacker.gui.streams.LogStream;

import javax.swing.*;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class UnpackerGUI implements UnpackerActions {

    private final LogStream logger;
    private final JFrame frame;

    public UnpackerGUI() {
        this.logger = new LogStream("UnpackerGUI");
        this.frame = new UnpackerJFrame(this, logger);
    }

    @Override
    public void unpack(File imageFile, File outputFolder) {
        logger.info("Unpacking " + imageFile.getAbsolutePath() + " to " + outputFolder.getAbsolutePath());
        ImageUnpacker.runAsync(imageFile, outputFolder, () -> logger.info("Finished unpacking!"));
    }

    @Override
    public void openExplorer(File folder) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(folder);
            } catch (IOException e) {
                logger.warn("Failed to open explorer window");
            }
        } else {
            logger.error("Desktop not supported");
        }
    }

    @Override
    public File selectInputFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            logger.info("Selected input file: " + selected.getAbsolutePath());
            return selected;
        }
        return null;
    }

    @Override
    public File selectOutputFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            logger.info("Selected output folder: " + selected.getAbsolutePath());
            return selected;
        }
        return null;
    }
}
