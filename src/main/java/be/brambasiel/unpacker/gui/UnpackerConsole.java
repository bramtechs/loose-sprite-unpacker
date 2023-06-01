package be.brambasiel.unpacker.gui;

import be.brambasiel.unpacker.gui.streams.LogStreamListener;

import java.awt.*;

public class UnpackerConsole extends TextArea implements LogStreamListener {
    public UnpackerConsole() {
        this.setEditable(false);
    }

    @Override
    public void receivedPushedText(String text) {
        this.append(text+"\n");
    }
}
