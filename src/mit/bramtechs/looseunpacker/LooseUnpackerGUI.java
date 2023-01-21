package mit.bramtechs.looseunpacker;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Button;
import java.awt.Desktop;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;

public class LooseUnpackerGUI extends JFrame {

	private static final long serialVersionUID = -4770437700973760346L;

	private String inputFile;
	private String outputFolder;

	private TextArea outputArea;

	public LooseUnpackerGUI() {
		super("Loose Sprite Unpacker");
		Dimension size = new Dimension(370, 350);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
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
		
		outputArea = new TextArea();
		outputArea.setBounds(10, 87, 339, 160);
		getContentPane().add(outputArea);
		setVisible(true);

		// binding
		extractButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputFile == null || outputFolder == null) {
					outputArea.append("Select input and output first!\n");
					return;
				}
				try {
					LooseUnpackerLauncher.main(new String[]{inputFile,outputFolder});
				} catch (Exception err) {
					outputArea.append("\nSomething went wrong\n");
					outputArea.append(err.toString());
					err.printStackTrace();
				}
			}
		});
		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (outputFolder != null) {
					File directory = new File(outputFolder);
					try {
						Desktop.getDesktop().open(directory);
					} catch (IOException err) {
						outputArea.append("\nCould not open explorer.\n");
						outputArea.append(err.toString());
						err.printStackTrace();
					}
				}
			}
		});

		inputSelectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int code = chooser.showOpenDialog(LooseUnpackerGUI.this);

				switch (code) {
				case JFileChooser.APPROVE_OPTION: 
					inputFile = getPathFromFile(chooser.getSelectedFile());
					printFolderPaths();
					break;
				default:
					break;
				}
			}
		});

		outputSelectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int code = chooser.showOpenDialog(LooseUnpackerGUI.this);

				switch (code) {
				case JFileChooser.APPROVE_OPTION: 
					outputFolder = getPathFromFile(chooser.getSelectedFile());
					printFolderPaths();
					break;
				default:
					break;
				}
			}
		});

	}
	
	private String getPathFromFile(File file) {
		String path = file.getAbsolutePath();
		path = path.replace('\\', '/');
		return path;
	}

	private void printFolderPaths() {
		outputArea.append("Set input file to: " + inputFile + "\n");
		outputArea.append("Set output folder to: " + outputFolder + "\n\n");
	}
}
