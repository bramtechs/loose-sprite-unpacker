package be.brambasiel.unpacker;

import be.brambasiel.unpacker.cli.ImageUnpackerCLI;
import be.brambasiel.unpacker.gui.UnpackerGUI;

public class ImageUnpackerEntry {

    public static void main(String[] args) {
        if (args.length == 0) {
            new UnpackerGUI();
            return;
        }
        new ImageUnpackerCLI(args);
    }

}
