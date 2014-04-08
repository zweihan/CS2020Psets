package pset6;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class WordMarkovModel {
	ArrayList<String> strList;
	int order;
	long randSeed;
	private Random rand;
	private HashMap<String,WordCounter> hMap = new HashMap<String,WordCounter>();
	
	WordMarkovModel(ArrayList<String> str,int num){
		strList = str;
		order= num;
		createHashMap();
		//random seed default to 0;
		randSeed=0;
		rand = new Random(randSeed);
		
	}
	//stores the count of occurrences of a character (ascii code mapped to integer k) in countArr[k].
	//countArr[128] stores number of occurrences of character after kgram. countArr[129] stores total number of occurrences of kgram.
	//creates a hashmap to map each kgram to its own countArr.
	private void createHashMap(){
		Iterator<String> itr =strList.iterator();
		//initialise kgram. 
		//Note: kgram is stored as a list<String>. This does not work with hashMap. 
		//I tried writing a wrapper class to override .equals and .hashCode methods such that if list contains the same strings, 
		//it will be equal and hash to the same range, but somehow it still doesn't work. So I designed a clunky solution.
		//Hence, kgram is converted to string before hashing.
		List<String> kgram=new ArrayList<String>();
		for(int i=0;i<order;i++){
			kgram.add(itr.next());
			}
		
		String nextStr = itr.next();
		//first iteration
		String kgramStr=new String();
		WordCounter wc = new WordCounter();
		wc.add(nextStr);
		for(int i=0;i<kgram.size();i++)
			kgramStr=kgramStr.concat(kgram.get(i));
		hMap.put(kgramStr, wc);
		System.out.println(kgram);
		//subsequent iteration
		while(itr.hasNext()){
			//reset kgram to next set of words
			kgram.remove(0);
			kgram.add(nextStr);
			nextStr=itr.next();
			kgramStr=new String();
			for(int i=0;i<kgram.size();i++)
				kgramStr=kgramStr.concat(kgram.get(i));
			
			if(hMap.containsKey(kgramStr)){
				wc=hMap.get(kgramStr);
				wc.add(nextStr);
				hMap.put(kgramStr, wc);
			}
			else{
				wc=new WordCounter();
				wc.add(nextStr);
				hMap.put(kgramStr, wc);
			}
		}
	}
	
	public int getFrequency(String kgram){
		if(kgram.length()!=order){
			throw new InvalidParameterException("kgram is of wrong order!");
		}
		WordCounter wc = hMap.get(kgram);
		return wc.totalCount();
	}
	
	public int getFrequency(String kgram,String s){
		if(kgram.length()!=order){
			throw new InvalidParameterException("kgram is of wrong order!");
		}
		WordCounter wc = hMap.get(kgram);
		return wc.contains(s);
	}
	
	
	public String nextWord(String kgram){
		
		//the method returns null here. This causes the generator to break if the input kgram has not occured before. 
		//Only happens for order>=2 and on some occasion only. The problem can be fixed by replacing return null to return some random string.
		if(!hMap.containsKey(kgram)){
			System.out.println(kgram);
			return null;
		}
		
		WordCounter wc = hMap.get(kgram);
		int randNum = rand.nextInt(wc.totalCount());
		int cumCount=0;
		String word = new String();
		Iterator<String> wordItr=wc.wordMap.keySet().iterator();
		while(cumCount<=randNum){
			
			word=wordItr.next();
			int count=wc.contains(word);
			cumCount+=count;
			
		}
		if(word==null)
			word=wordItr.next();
			
		
		return word;
		
		
	}
	
	void setRandomSeed(long s){
		randSeed=s;
		rand = new Random(randSeed);
	}
		
		
	//WordCounter class encapsulates all the information needed to count word occurrences after kgram.
	//itself encapsulates a hashmap that maps a particular word to its count. 
	private class WordCounter{
		HashMap<String,Integer> wordMap = new HashMap<String,Integer>();		
		//total number of count of occurrences
		int numWords;
		WordCounter(){
		numWords=0;
		}
		public int contains(String word){
			if(wordMap.containsKey(word))
				return wordMap.get(word);
			else 
				return 0;
		}
		public void add(String word){
			if(wordMap.containsKey(word)){
				int num =wordMap.get(word);
				num++;
				wordMap.put(word, num);
				numWords++;
			}
			else{
			wordMap.put(word, 1);
			numWords++;
			}
		}
		public int totalCount(){
			return numWords;
		}	
	}

	
}