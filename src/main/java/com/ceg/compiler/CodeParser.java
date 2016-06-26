package com.ceg.compiler;

import java.util.ArrayList;


public class CodeParser {
	
	// dopisuje "cout<<endl" 
	// za kazdym znalezionym wywolaniem "cout" 
	// zwraca liczbe znalezionych coutow
	// IN: List<String> lines - wejsciowa lista linii kodu
	// OUT: List<String> result - wynikowa lista linii kodu
	static public int addNewlineAfterEachCout(ArrayList<String> lines, ArrayList<String> result){
		int couts = 0;
		result.addAll(lines); // skopiuj liste wejsciowa
		
		for (int index = 0 ; index < result.size(); index++) {
			String line = result.get(index);
			if(line.contains("cout <<") || line.contains("cout<<")){
				couts++;
				result.add(++index, "cout<<endl;");
			}
		}
		return couts;
	}
	
	static public int deleteOtherCouts(int lineNo, ArrayList<String> code){
            int count = 0;
            return count;
        }
	
}
