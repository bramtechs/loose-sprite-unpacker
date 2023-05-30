package be.brambasiel.unpacker.gui;

import java.io.File;

interface UnpackerActions {
    void unpack(File inputFile, File outputFolder);
    void openExplorer(File folder);
    File selectInputFile();
    File selectOutputFile();
}
