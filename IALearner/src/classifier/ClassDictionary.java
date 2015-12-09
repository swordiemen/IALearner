package classifier;

import java.util.HashMap;
import java.util.List;

public class ClassDictionary {
	private String className;
	private HashMap<String, Integer> dict;
	private int totalLength;
	private int smoothing;
	
	public ClassDictionary(String name){
		setClassName(name);
		dict = new HashMap<String, Integer>();
	}
	
	public int getTotal(){
		return totalLength;
	}
	
	public void incTotal(){
		totalLength++;
	}
	
	public void insert(List<String> words){
		for(String word : words){
			if(dict.containsKey(word)){
				dict.put(word, dict.get(word) + 1); 
			}else{
				dict.put(word, 1);
			}
			incTotal();
		}
	}
	
	public double prob(String word){
		int occurrence = dict.containsKey(word) ? dict.get(word) : 0;
		double prob = (double) (occurrence + smoothing) / (getTotal() + dict.size() * smoothing);
		//System.out.println("Woord: " + word + " prob: " + prob);
		return /*Math.log(prob) / Math.log(2);*/prob;
	}
	
	public double probSentence(List<String> words){
		double prob = 0;
		//System.out.println(words.size());
		for(String word : words){
			prob += prob(word);
		}
		return prob;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	public HashMap<String, Integer> getDict(){
		return dict;
	}
	
	
}
