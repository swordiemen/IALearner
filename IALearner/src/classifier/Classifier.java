package classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Classifier {
	private HashMap<String, List<ClassDictionary>> categories;
	private String currentCategorie;
	private Tokenizer tokenizer;
	private String punten = "."; //can be 1 dot or 2 dots depending on your system.

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
		double maxProbe = Double.NEGATIVE_INFINITY;
		String maxClass = "";
		for (ClassDictionary dictionary : categories.get(currentCategorie)) {
			double prob = dictionary.probSentence(tokenizer.getTokens(f));
			System.out.println("ProbSentence for " + dictionary.getClassName() + ": " + prob);
			if (prob > maxProbe) {
				maxProbe = prob;
				maxClass = dictionary.getClassName();
			}
		}
		return maxClass;
	}

	public static void main(String[] args) {
		int succeeds = 0;
		int f = 0;
		String punten = "."; //can be 1 dot or 2 dots depending on your system.
		Classifier c = new Classifier();
		c.createCategorie("blogs", new String[] { "F", "M" });
		c.selectCurrentClass("blogs");
		for (int i = 1; i < 600; i++) {
			if(new File(punten + "/blogs/F/F-train" + i
					+ ".txt").exists()){
			c.train(new File(punten + "/blogs/F/F-train" + i + ".txt"), "F");
			}else{
			c.train(new File(punten + "/blogs/M/M-train" + i + ".txt"), "M");
			}
		}
		System.out.println(c.getClass("F").getTotal());
		System.out.println(c.getClass("M").getTotal());
		for (int i = 1; i < 50; i++) {
			System.out.print("Testing test" + i);
			if(new File(punten + "/blogs/F/F-test" + i
					+ ".txt").exists()){
				System.out.print(" (F)\n");
				boolean succeeded = (c.classify(new File(punten + "/blogs/F/F-test" + i
						+ ".txt"))).equals("F");
				if(succeeded){
					succeeds++;
					f++;
				}
				System.out.println(succeeded);
			}else{
				System.out.print(" (M)\n");
				boolean succeeded = c.classify(
						new File(punten + "/blogs/M/M-test" + i + ".txt"))
						.equals("M");
				if(succeeded){
					succeeds++;
				}
				System.out.println(succeeded);
			}
		}
		//System.out.println(c.classify(new File(punten + "/blogs/F/F-test2.txt")));
		System.out.println("Succeeds: " + succeeds + " of 49, of which " + f + " were f");
	}

}
