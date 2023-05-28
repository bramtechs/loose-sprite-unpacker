package be.brambasiel.unpacker.gui;

import java.io.File;

public interface UnpackerActions {
    void unpack(File inputFile, File outputFolder);
    void openExplorer(File folder);
    void selectInputFile();
    void selectOutputFile();
}
