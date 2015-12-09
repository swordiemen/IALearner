package classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Classifier {
	private HashMap<String, List<ClassDictionary>> categories;
	private String currentCategorie;
	private Tokenizer tokenizer;

	public Classifier() {
		tokenizer = new Tokenizer();
		categories = new HashMap<String, List<ClassDictionary>>();
	}

	public void selectCurrentClass(String subject) {
		currentCategorie = subject;
	}

	public void train(File text, String dicClass) {
		List<String> words = tokenizer.getTokens(text);
		for (ClassDictionary dictionary : categories.get(currentCategorie)) {
			if (dictionary.getClassName().equals(dicClass)) {
				dictionary.insert(words);
			}
		}
	}

	public void createCategorie(String name, String[] classes) {
		List<ClassDictionary> classList = new ArrayList<ClassDictionary>();
		for (String group : classes) {
			classList.add(new ClassDictionary(group));
		}
		categories.put(name, classList);
	}

	public ClassDictionary getClass(String className) {
		for (ClassDictionary classes : categories.get(currentCategorie)) {
			if (classes.getClassName().equals(className)) {
				return classes;
			}
		}
		return null;
	}

	public String classify(File f) {
		double maxProbe = -1;
		String maxClass = "";
		for (ClassDictionary dictionary : categories.get(currentCategorie)) {
			System.out.println(dictionary.probSentence(tokenizer.getTokens(f)));
			if (dictionary.probSentence(tokenizer.getTokens(f)) > maxProbe) {
				maxProbe = dictionary.probSentence(tokenizer.getTokens(f));
				maxClass = dictionary.getClassName();
			}
		}
		return maxClass;
	}

	public static void main(String[] args) {
		Classifier c = new Classifier();
		c.createCategorie("blogs", new String[] { "F", "M" });
		c.selectCurrentClass("blogs");
		for (int i = 1; i < 600; i++) {
			if(new File("../blogs/F/F-train" + i
					+ ".txt").exists()){
			c.train(new File("../blogs/F/F-train" + i + ".txt"), "F");
			}else{
			c.train(new File("../blogs/M/M-train" + i + ".txt"), "M");
			}
		}
		System.out.println(c.getClass("F").getTotal());
		System.out.println(c.getClass("M").getTotal());
		for (int i = 1; i < 50; i++) {
			if(new File("../blogs/F/F-test" + i
					+ ".txt").exists()){
				System.out.println((c.classify(new File("../blogs/F/F-test" + i
						+ ".txt"))).equals("F"));
			}else{
				System.out.println(c.classify(
						new File("../blogs/M/M-test" + i + ".txt"))
						.equals("M"));
			}
		}
		System.out.println(c.classify(new File("../blogs/F/F-test2.txt")));
	}

}
