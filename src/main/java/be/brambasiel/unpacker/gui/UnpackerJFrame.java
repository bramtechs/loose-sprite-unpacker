package be.brambasiel.unpacker.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class UnpackerJFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = -4770437700973760346L;
    private static final Logger logger = Logger.getLogger(UnpackerJFrame.class.getName());
    private final TextArea console;
    private final UnpackerActions actions;
    private final Set<UpdateFunction> updators;

    private File inputFile;
    private File outputFolder;

    public UnpackerJFrame(UnpackerActions actions) {
        super("Loose Sprite Unpacker");
        this.updators = new HashSet<>();
        this.actions = actions;
        this.configure();
        this.initLabels();
        this.initButtons();
        this.console = initConsole();
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

    private TextArea initConsole() {
        TextArea consoleTextArea = new TextArea();
        consoleTextArea.setBounds(10, 87, 339, 160);
        getContentPane().add(consoleTextArea);
        return consoleTextArea;
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

        JButton openButton = new JButton("Open output folder");
        openButton.setBounds(10, 260, 183, 37);
        getContentPane().add(openButton);
        openButton.addActionListener(e -> {
            if (outputFolder != null) {
                actions.openExplorer(outputFolder);
            }
        });

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
