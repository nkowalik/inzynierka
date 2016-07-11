package com.ceg.compiler;

import java.util.List;
import java.util.ListIterator;


public class CodeParser {
	
	// dopisuje "cout<<endl" 
	// za kazdym znalezionym wywolaniem "cout" 
	// zwraca liczbe znalezionych coutow
	// INOUT: List<String> lines - wejsciowa lista linii kodu
	static public int addNewlineAfterEachCout(List<String> lines){
            int couts = 0;
            try{
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
            }
            catch (IndexOutOfBoundsException e) {
                System.err.println("IndexOutOfBoundsException: " + e.getMessage());
            }
            return couts;
		
	}
	
        // usuwa wszystkie linie, w których wystąpił "cout" lub "printf"
        // poza liniami o numerach zawartych w liście lineNo
        // IN: List lineNo - lista numerów linii, których nie należy usuwać
        // INOUT: List<String> code - lista linii kodu
	static public void deleteOtherCouts(List lineNo, List<String> code){
            try{
                for (ListIterator<String> iterator = code.listIterator(); iterator.hasNext() ;)
                {
                    String str = iterator.next();
                    if ((!lineNo.contains(iterator.nextIndex()-1))  && (str.contains("cout <<") || str.contains("cout<<") || str.contains("printf(")))
                    {
                        iterator.remove();
                    }
                }
            }
            catch (IndexOutOfBoundsException e) {
                System.err.println("IndexOutOfBoundsException: " + e.getMessage());
            }
        }
	
}
