package com.ceg.compiler;

import java.util.List;
import java.util.ListIterator;


public class CodeParser {
	
	// dopisuje "cout<<endl" 
	// za kazdym znalezionym wywolaniem "cout" 
	// zwraca liczbe znalezionych coutow
	// IN: List<String> lines - wejsciowa lista linii kodu
	// OUT: List<String> result - wynikowa lista linii kodu
	static public int addNewlineAfterEachCout(List<String> lines, List<String> result){
            int couts = 0;
            for (ListIterator<String> iterator = lines.listIterator(); iterator.hasNext() ;)
            {
                String str = iterator.next();
                if (str.contains("cout <<") || str.contains("cout<<"))
                {
                    iterator.add("cout<<endl;");
                    couts++;
                }
                else if(str.contains("printf")){
                    iterator.add("printf(\"\\n\");");
                    couts++;
                }
            }
            result = lines;
            return couts;
		
	}
	
	static public int deleteOtherCouts(int lineNo, List<String> code){
            int count = 0;
            return count;
        }
	
}
