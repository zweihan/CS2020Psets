package pset6;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Random;

public class MarkovModel {
	String str;
	int order;
	long randSeed;
	private Random rand;
	private HashMap<String,int[]> hMap = new HashMap<String,int[]>();
	
	MarkovModel(String string,int num){
		str=string;
		order=num;
		createHashMap();
		//random seed default to 0;
		randSeed=0;
		rand = new Random(randSeed);
		
	}
	//stores the count of occurrences of a character (ascii code mapped to integer k) in countArr[k].
	//countArr[128] stores number of occurrences of character after kgram. countArr[129] stores total number of occurrences of kgram.
	//creates a hashmap to map each kgram to its own countArr.
	private void createHashMap(){
		int strlen = str.length();
		//initialise first kgram
		StringBuffer strBuffer=new StringBuffer();
		for(int i=0;i<order;i++){
			strBuffer.append(str.charAt(i));
		}
		int[] countArr = new int[130];
		//first iteration
		char nextChar=str.charAt(order);
		countArr[nextChar]=1;
		countArr[128]=1;
		countArr[129]=1;
		hMap.put(strBuffer.toString(), countArr);
		
		//repeat for the length of the entire string
		for(int i =order+1;i<strlen;i++){
			strBuffer.deleteCharAt(0);
			strBuffer.append(nextChar);
			nextChar=str.charAt(i);

			if(hMap.containsKey(strBuffer.toString())){
				countArr=hMap.get(strBuffer.toString());
				countArr[nextChar]++;
				countArr[129]++;
				countArr[128]++;
				hMap.put(strBuffer.toString(), countArr);	
			}
			else{
				countArr = new int[130];
				countArr[nextChar]++;
				countArr[129]++;
				countArr[128]++;
				hMap.put(strBuffer.toString(),countArr);
			}
			//correction for last entry
			if(i==str.length()-1){
				strBuffer.deleteCharAt(0);
				strBuffer.append(nextChar);
				if(hMap.containsKey(strBuffer.toString())){
					countArr=hMap.get(strBuffer.toString());
					countArr[129]++;
					hMap.put(strBuffer.toString(), countArr);	
				}
				else{
					countArr = new int[130];
					countArr[129]++;
					hMap.put(strBuffer.toString(),countArr);
				}
			}
		}
	}
	
	public int getFrequency(String kgram){
		if(kgram.length()!=order){
			throw new InvalidParameterException("kgram is of wrong order!");
		}
		int[] countArr = hMap.get(kgram);
		return countArr[129];
	}
	
	public int getFrequency(String kgram,char c){
		if(kgram.length()!=order){
			throw new InvalidParameterException("kgram is of wrong order!");
		}
		int[] countArr = hMap.get(kgram);
		return countArr[c];
	}
	
	public char nextCharacter(String kgram){
		if(kgram.length()!=order){
			throw new InvalidParameterException("kgram is of wrong order!");
		}
		
		int[] countArr=hMap.get(kgram);
		//returns a 'space' character if kgram does not exist in original text or no next character is possible.
		if(countArr==null)
			return (char) (32);
		
		if(countArr[128]==0)
			return (char) (32);
		//generate a random integer from 0 to total number of occurrences of a character after kgram
		int randNum=rand.nextInt(countArr[128]);
		int cumCount=0;
		int i=-1;
		
		//count keeps a cumulative count of how many times a character has occurred from ascii code '0' onwards. 
		//when randNum is less than cumCount, it returns the corresponding i-th position of the count that exceeds randNum.
		//this should map a uniformly distributed R.V (randNum) to the Markov probabilities of character occurring after kgram.
		while(cumCount<=randNum){
			i++;
			cumCount+=countArr[i];
			
		}
		if(i==-1)
			i++;
		
		return (char)i;
		
	}
	
	
	public int order(){
		return order;
	}
	
	
	public void setRandomSeed(long s){
		randSeed=s;
		rand = new Random(randSeed);
	}	
	
}
