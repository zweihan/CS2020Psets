package pset6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;

public class TextGenerator {
	public static void main(String[] args) throws IOException{
		//parse arguments
		if(args.length!=3)
			throw new InvalidParameterException("invalid number of parameters. Parameters should be as follows: order, lengthOfTextToGenerate, fileName.");
		int order = Integer.parseInt(args[0]);
		int generateSize = Integer.parseInt(args[1]);
		FileReader fileName=new FileReader(args[2]);
		
		//read in text file 1 character at a time and add it to string buffer. Whole text parsed as string.
		BufferedReader bReader = new BufferedReader(fileName);
		char c;
		StringBuffer str = new StringBuffer();
		while((c=(char) bReader.read())>=0&&c<=128){
			if(c=='\n')
				continue;
			else 
				str.append(c);
		}
		
		bReader.close();
		
		MarkovModel mm = new MarkovModel(str.toString(),order);
		//random seed to generate different texts.
		mm.setRandomSeed(10);
		//stringBuffer to store generated texts
		StringBuffer kgram = new StringBuffer(str.substring(0,order));
		StringBuffer stringBuilder = new StringBuffer(kgram);
		
		for(int i=0;i<generateSize;i++){
			
			c=mm.nextCharacter(kgram.toString());
			kgram.deleteCharAt(0);
			kgram.append(c);
			stringBuilder.append(c);
			
			//conditionals to break texts into lines to make it more readable. It is not a neat solution and is probabilistic. 
			if((i%13==0||i%17==0)&&c==' ')
				stringBuilder.append('\n');
		}
		System.out.println("Generated Text:");
		System.out.println(stringBuilder.toString());
		
	
	}
	
}
