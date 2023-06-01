package be.brambasiel.unpacker.gui;

import be.brambasiel.unpacker.gui.streams.LogStream;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

public class UnpackerJFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = -4770437700973760346L;

    private final LogStream logger;
    private final UnpackerActions actions;
    private final Set<UpdateFunction> updators;
    private File inputFile;
    private File outputFolder;

    public UnpackerJFrame(UnpackerActions actions, LogStream logger) {
        super("Loose Sprite Unpacker");
        this.logger = logger;
        this.updators = new HashSet<>();
        this.actions = actions;
        this.configure();
        this.initLabels();
        this.initButtons();
        this.initConsole();
        this.setVisible(true);
        this.refresh();
    }

    private void configure() {
        Dimension size = new Dimension(370, 350);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
    }

    private void initConsole() {
        UnpackerConsole console = new UnpackerConsole();
        logger.addListener(console);
        console.setBounds(10, 87, 339, 160);
        getContentPane().add(console);
    }

    private void refresh() {
        updators.forEach(UpdateFunction::update);
    }

    private void initLabels() {
        JLabel inputFileLabel = new JLabel("Input file");
        inputFileLabel.setBounds(20, 16, 125, 19);
        getContentPane().add(inputFileLabel);
        updators.add(() -> {
            String name = (inputFile == null ? "none" : inputFile.getName());
            inputFileLabel.setText("Input file: " + name);
        });

        JLabel outputFolderLabel = new JLabel("Output folder");
        outputFolderLabel.setBounds(20, 46, 125, 27);
        getContentPane().add(outputFolderLabel);
        updators.add(() -> {
            String name = outputFolder == null ? "none" : outputFolder.getName();
            outputFolderLabel.setText("Output folder: " + name);
        });
    }

    private void initButtons() {
        JButton extractButton = new JButton("Extract");
        extractButton.setBounds(224, 260, 125, 37);
        getContentPane().add(extractButton);
        updators.add(() -> extractButton.setEnabled(inputFile != null && outputFolder != null));
        extractButton.addActionListener(e -> {
            actions.unpack(inputFile, outputFolder);
        });

        JButton openButton = new JButton("Open output folder");
        openButton.setBounds(10, 260, 183, 37);
        getContentPane().add(openButton);
        openButton.addActionListener(e -> {
            if (outputFolder != null) {
                actions.openExplorer(outputFolder);
            }
        });
        updators.add(() -> openButton.setEnabled(outputFolder != null));

        Button inputSelectButton = new Button("Select");
        inputSelectButton.setBounds(264, 16, 70, 22);
        getContentPane().add(inputSelectButton);
        inputSelectButton.addActionListener(e -> {
            inputFile = actions.selectInputFile();
            refresh();
        });

        Button outputSelectButton = new Button("Select");
        outputSelectButton.setBounds(264, 44, 70, 22);
        getContentPane().add(outputSelectButton);
        outputSelectButton.addActionListener(e -> {
            outputFolder = actions.selectOutputFile();
            refresh();
        });
    }

}
