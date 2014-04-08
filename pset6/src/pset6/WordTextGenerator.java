package pset6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class WordTextGenerator {
	public static void main(String[] args) throws IOException{
		System.out.println("start");
		//read in arguments.
		if(args.length!=3)
			throw new InvalidParameterException("invalid number of parameters. Parameters should be as follows: order, lengthOfTextToGenerate, fileName.");
		int order = Integer.parseInt(args[0]);
		int generateSize = Integer.parseInt(args[1]);
		FileReader fileName=new FileReader(args[2]);
		BufferedReader bReader = new BufferedReader(fileName);
		String string;
		
		//read in txt file line by line, parse them into string tokens and store the string tokens in an ArrayList
		ArrayList<String> strList = new ArrayList<String>();
		while((string=bReader.readLine())!=null){
			string=string.toLowerCase();
			StringTokenizer strTokenizer = new StringTokenizer(string, " ;:<>/][)({}\n");
			while(strTokenizer.hasMoreTokens()){
				String strings= strTokenizer.nextToken();
				strList.add(strings);
			}
		}
		bReader.close();
		//pass in the arraylist of string tokens we have created from the textfile from above to the markov model
		WordMarkovModel mm = new WordMarkovModel(strList,order);
		
		//random seed to generate different texts
		mm.setRandomSeed(0);
		
		//initialise;
		List<String> kgram = new ArrayList<String>();
		StringBuffer strOut=new StringBuffer();
		for(int i=0;i<order;i++){
			kgram.add(strList.get(i));
			strOut.append(strList.get(i));
			strOut.append(" ");
		}
			
		//generate randome texts based on markov model by calling one word as a time		
		for(int i=0;i<generateSize;i++){
			String kgramStr = new String();
			for(int j=0;j<kgram.size();j++){
				kgramStr=kgramStr.concat(kgram.get(j));
			}
			String str = mm.nextWord(kgramStr);
			kgram.remove(0);
			kgram.add(str);
			strOut.append(str);
			//some conditions to break the sentences into lines
			if(i%10==0){
				strOut.append("\n");
			} else{
				strOut.append(" ");
			}
		}
		System.out.println("Generated Text:");
		System.out.println(strOut.toString());
		
	
	}
	
}
