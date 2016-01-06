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
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.FileChooserUI;

public class ClassifierGUI extends JFrame implements Constants {
	final JFileChooser fc = new JFileChooser();
	private JButton trainButton;
	private JButton testButton;
	private JButton fileChooser;
	private JPanel buttons;
	private JLabel[] labels = new JLabel[3];
	private Classifier classifier = new Classifier();
	private final String dots = DOTS;
	private boolean fileSelected = false;

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
		trainButton = new JButton("Train");
		testButton = new JButton("Test");
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
					fileSelected = true;
				}
			}
		});
		filechooserAndLabel.add(fileChooser);
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel();
			// frame.add(labels[i]);
		}
		labels[0].setText("Select a file and press train or test.");
		labels[0].setAlignmentX(CENTER_ALIGNMENT);
		trainButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (fileSelected) {
					File file = fc.getSelectedFile();
					checkCorrect(classifier.classify(file), file);

				} else {
					labels[0].setText("No file selected yet");
				}
			}
		});
		testButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (fileSelected) {
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
		buttons.add(trainButton);
		System.out.println(testButton.getLocation().y);
		System.out.println(testButton.getSize().height);
		buttons.add(testButton);
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
					getCorrectClassFromUser(fc.getSelectedFile());
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
					getCorrectClassFromUser(fc.getSelectedFile());
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
	
	public void getCorrectClassFromUser(File file){
		List<ClassDictionary> classList = classifier.getCurrentClasses();
		String[] classStringList = new String[classList.size() + 1]; //+1 for a possible new class
		for(int i = 0; i < classStringList.length - 1; i++){
			classStringList[i] = classList.get(i).getClassName();
		}
		classStringList[classStringList.length - 1] = "Make new class";
		Tokenizer tokenizer = new Tokenizer();
		List<String> tokens = tokenizer.getTokens(file);
		int classChoice = JOptionPane.showOptionDialog(this, "What class should it belong to?", "class",
				JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				classStringList, 2);
		if(classChoice < classStringList.length - 1){
			classList.get(classChoice).insert(tokens);
		}else{
			String newName = JOptionPane.showInputDialog("New class name:");
			classifier.createClass(newName);
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