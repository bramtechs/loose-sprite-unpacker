package be.brambasiel.unpacker.gui;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.logging.Logger;

public class UnpackerJFrame extends JFrame {

    private static final Logger logger = Logger.getLogger(UnpackerJFrame.class.getName());

    @Serial
    private static final long serialVersionUID = -4770437700973760346L;

    private final TextArea console;

    public UnpackerJFrame() {
        super("Loose Sprite Unpacker");
        Dimension size = new Dimension(370, 350);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        initComponents();
        console = initConsole();

        setVisible(true);
    }

    private TextArea initConsole() {
        TextArea consoleTextArea = new TextArea();
        consoleTextArea.setBounds(10, 87, 339, 160);
        getContentPane().add(consoleTextArea);
        return consoleTextArea;
    }

    private void initComponents() {
        JLabel inputFileLabel = new JLabel("Input file");
        inputFileLabel.setBounds(20, 16, 125, 19);
        getContentPane().add(inputFileLabel);

        JButton extractButton = new JButton("Extract");
        extractButton.setBounds(224, 260, 125, 37);
        getContentPane().add(extractButton);

        JButton openButton = new JButton("Open output folder");
        openButton.setBounds(10, 260, 183, 37);
        getContentPane().add(openButton);

        JLabel outputFolderLabel = new JLabel("Output folder");
        outputFolderLabel.setBounds(20, 46, 125, 27);
        getContentPane().add(outputFolderLabel);

        Button inputSelectButton = new Button("Select");
        inputSelectButton.setBounds(264, 16, 70, 22);
        getContentPane().add(inputSelectButton);

        Button outputSelectButton = new Button("Select");
        outputSelectButton.setBounds(264, 44, 70, 22);
        getContentPane().add(outputSelectButton);
    }

}
