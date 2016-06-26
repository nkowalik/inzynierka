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
	static public int addNewlineAfterEachCout(List<String> lines, List<String> result){
		int couts = 0;
		result.addAll(lines); // skopiuj liste wejsciowa
		for(String str : result){
                    if(str.contains("cout<<endl;") || str.contains("printf(\"\\n\"")){
                        continue;
                    }
                    else if(str.contains("cout <<") || str.contains("cout<<")){
				couts++;
                            result.add(result.indexOf(str)+1, "cout<<endl;");
                    }
                    else if(str.contains("printf(")){
                            couts++;
                            String s = "printf(\"\\n\");";
                            result.add(result.indexOf(str)+1, s);
                    }
                }
//		for (int index = 0 ; index < lines.size(); index++) {
//			String line = lines.get(index);
//			if(line.contains("cout <<") || line.contains("cout<<")){
//				couts++;
//				result.add(++index, "cout<<endl;");
//			}
  //                      else if(line.contains("printf(")){
//				couts++;
  //                              String str = "printf(\"\\n\");";
//				result.add(++index, str);
//			}
//		}
		return couts;
	}
	
        static public int deleteOtherCouts(int lineNo, ArrayList<String> code){
            int count = 0;
            return count;
        }
	
}
