package com.ceg.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
	
	// TEMP !!!
	// modyfikuje kod zapisany w pliku .cpp na dysku	
	static public int addNewlineAfterEachCoutFromFile(String pathSrc, String pathDest){
		List<String> lines = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		int couts = 0;
		try {
			lines = Files.readAllLines(Paths.get(pathSrc));
			couts = CodeParser.addNewlineAfterEachCout((ArrayList<String>)lines, (ArrayList<String>)result);
			Files.write(Paths.get(pathDest), result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return couts;
	}
	
}
