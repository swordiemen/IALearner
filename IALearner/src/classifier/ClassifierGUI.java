package classifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.plaf.FileChooserUI;

public class ClassifierGUI extends JFrame implements Constants {
	final JFileChooser fc = new JFileChooser();
	private JButton button1;
	private JButton button2;
	private JButton fileChooser;
	private JPanel buttons;
	private JLabel[] labels = new JLabel[3];
	private Classifier classifier = new Classifier();
	private final String dots = DOTS;
	private boolean FileSelected = false;

	public ClassifierGUI() {
		super("ClassifierGUI");
		int classChoice = JOptionPane.showOptionDialog(this,
				"Choose your class", "class", JOptionPane.OK_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, new String[] { "Mails",
						"Blogs" }, 2);
		if (classChoice == 0) {
			classifier.startMails();
		} else {
			classifier.startBlogs();
		}
		setSize(600, 300);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
	}

	public void init() {
		button1 = new JButton("Train");
		button2 = new JButton("Test");
		buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		JPanel filechooserAndLabel = new JPanel();
		filechooserAndLabel.setLayout(new BoxLayout(filechooserAndLabel,
				BoxLayout.Y_AXIS));
		fileChooser = new JButton("Choose File");
		fileChooser.setAlignmentX(CENTER_ALIGNMENT);
		fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnval = fc.showOpenDialog(ClassifierGUI.this);
				if (returnval == JFileChooser.APPROVE_OPTION) {
					FileSelected = true;
				}
			}
		});
		filechooserAndLabel.add(fileChooser);
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel();
			// frame.add(labels[i]);
		}
		labels[0].setText("Select a file and press train of test.");
		labels[0].setAlignmentX(CENTER_ALIGNMENT);
		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (FileSelected) {
					File file = fc.getSelectedFile();
					checkCorrect(classifier.classify(file), file);

				} else {
					labels[0].setText("No file selected yet");
				}
			}
		});
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (FileSelected) {
					String result = classifier.classify(fc.getSelectedFile());
					int correctAnswer = JOptionPane.showOptionDialog(
							ClassifierGUI.this, "The classifier thinks it is: "
									+ result, "class", JOptionPane.OK_OPTION,
							JOptionPane.QUESTION_MESSAGE, null,
							new String[] { "OK" }, 2);
				}else{
					labels[0].setText("No file selected.");
				}
			}
		});
		filechooserAndLabel.add(labels[0]);
		add(filechooserAndLabel, BorderLayout.NORTH);
		buttons.add(button1);
		System.out.println(button2.getLocation().y);
		System.out.println(button2.getSize().height);
		buttons.add(button2);
		add(buttons, BorderLayout.SOUTH);
	}

	public void checkCorrect(String result, File file) {
		String directory = "";
		int classChoice = JOptionPane.showOptionDialog(this, "The result is "
				+ result + ", is that correct?", "class",
				JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				new String[] { "Yes", "No" }, 2);
		if (classifier.getCurrentClassName().equals("mails")) {
			directory = dots + "/corpus-mails/corpus/TrainFiles/";
		} else if (classifier.getCurrentClassName().equals("blogs")) {
			directory = dots + "/blogs/TrainFiles/";
		}
		String filename = "";
		if (!directory.equals("")) {
			if (classifier.getCurrentClasses().get(0).getClassName()
					.equals(result)) {
				if (classChoice == 0) {
					filename = directory
							+ classifier.getCurrentClasses().get(0)
									.getFileName()
							+ (int) (Math.random() * 1000) + ".txt";

				} else {
					filename = directory
							+ classifier.getCurrentClasses().get(1)
									.getFileName()
							+ (int) (Math.random() * 1000) + ".txt";
				}
			} else {
				if (classChoice == 0) {
					filename = directory
							+ classifier.getCurrentClasses().get(1)
									.getFileName() + (int) Math.random() * 1000
							+ ".txt";
				} else {
					filename = directory
							+ classifier.getCurrentClasses().get(0)
									.getFileName() + (int) Math.random() * 1000
							+ ".txt";
				}
			}
		}
		try {
			if (!filename.equals("")) {
				if (new File(filename).createNewFile()) {
					PrintWriter writer = new PrintWriter(filename, "UTF-8");
					writer.print(readFile(file.getAbsolutePath(),
							StandardCharsets.UTF_8));
					System.out.println("the File"
							+ readFile(file.getAbsolutePath(),
									StandardCharsets.UTF_8));
					writer.close();
				} else {
					System.err.println("Error: File name is empty.");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public void chooseClass() {

	}

	public static void main(String[] args) {
		new ClassifierGUI().setVisible(true);
	}
}