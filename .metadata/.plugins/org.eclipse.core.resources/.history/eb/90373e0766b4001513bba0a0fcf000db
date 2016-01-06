package classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
	private List<String> stopwords;

	public Tokenizer(){
		stopwords = new ArrayList<String>();
		stopwords.add("");
		initStopWords();
	}

	public void initStopWords(){
		BufferedReader br;
		// https://code.google.com/p/stop-words/
		String[] files = {
				"./IALearner/src/classifier/stopwords/stop-words_english_1_en.txt",
				"./IALearner/src/classifier/stopwords/stop-words_english_2_en.txt",
				"./IALearner/src/classifier/stopwords/stop-words_english_3_en.txt",
				"./IALearner/src/classifier/stopwords/stop-words_english_4_en.txt",
				"./IALearner/src/classifier/stopwords/stop-words_english_5_en.txt",
				"./IALearner/src/classifier/stopwords/stop-words_english_6_en.txt"
		};
		try {
			for(int i = 0; i < files.length; i++){
				String file = files[i];
				br = new BufferedReader(new FileReader(file));
				String line = br.readLine();

				while(line != null){
					stopwords.add(line);
					line = br.readLine();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getStopwords(){
		return stopwords;
	}

	public List<String> getTokens(String text){
		// http://stackoverflow.com/questions/18830813/how-can-i-remove-punctuation-from-input-text-in-java
		text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase(); 
		String[] words = text.split(" ");
		ArrayList<String> res = new ArrayList<String>();
		for(String word : words){
			if(!stopwords.contains(word)){
				res.add(word);
			}
		}
		//System.out.println(res.size());
		return res;
	}

	public List<String> getTokens(File file){
		StringBuilder sb = new StringBuilder();
		BufferedReader br;
		try{
			
			br = new BufferedReader(new FileReader(file));
			//System.out.println("Line");
			String line = br.readLine();
			
			while(line != null){
				sb.append(line);
				line = br.readLine();
			}
			br.close();
		}catch(IOException e){
			//e.printStackTrace();
			//System.out.println("Error");
		}
		//System.out.println(sb.toString());
		return getTokens(sb.toString());
	}

	public static void main(String[] args){
		Tokenizer t = new Tokenizer();
		System.out.println(t.getStopwords().toString());
		List<String> test = t.getTokens("Heyo, you wanna go bowling Saturday? I feel like bowling.");
		System.out.println(test);
		System.out.println(test.size());
		List<String> test2 = t.getTokens(new File("./src/classifier/stopwords/test.txt"));
		System.out.println(test2);
		System.out.println(test2.size());
	}
}
