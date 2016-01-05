package classifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;

import javax.swing.*;
import javax.swing.plaf.FileChooserUI;

public class ClassifierGUI extends JFrame {
	final JFileChooser fc = new JFileChooser();
	private JButton button1;
	private JButton button2;
	private JButton fileChooser;
	private JPanel buttons;
	private JLabel[] labels = new JLabel[3];
	private JTextArea textarea;

	public ClassifierGUI() {
		super("ClassifierGUI");
		int classChoice = JOptionPane.showOptionDialog(this, "Choose your class", "class",
				  JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {
			      "Mails", "Blogs"}, 2);
		if(classChoice = 0){
			classfier.startMails();
		}
		else{
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
		filechooserAndLabel.setLayout(new BoxLayout(filechooserAndLabel,BoxLayout.Y_AXIS));
		fileChooser = new JButton("Choose File");
		fileChooser.setAlignmentX(CENTER_ALIGNMENT);
		filechooserAndLabel.add(fileChooser);
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel();
			// frame.add(labels[i]);
		}

		labels[0].setText("OR: Enter a classifiable sentence");
		labels[0].setAlignmentX(CENTER_ALIGNMENT);
		filechooserAndLabel.add(labels[0]);
		add(filechooserAndLabel,BorderLayout.NORTH);
		textarea = new JTextArea(20, 5);
		add(new JScrollPane(textarea),BorderLayout.CENTER);
		System.out.println(textarea.getRows());
		textarea.setText("Test");
		buttons.add(button1);
		System.out.println(button2.getLocation().y);
		System.out.println(button2.getSize().height);
		buttons.add(button2);
		add(buttons,BorderLayout.SOUTH);
	}
	public void chooseClass(){
		
	}
	public static void main(String[] args) {
		new ClassifierGUI().setVisible(true);
	}
}