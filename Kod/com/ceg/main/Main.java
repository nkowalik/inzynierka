package com.ceg.main;

import java.util.ArrayList;
import java.util.List;

import com.ceg.compiler.Ccompiler;
import com.ceg.compiler.CodeParser;

public class Main {
	// dodaje cout<<endl; po każdym cout, kompiluje, wykonuje i pobieram output do listy Stringów
	  public static void main(String[] args) {        
	        Ccompiler compiler = new Ccompiler(); 
	        String filename = "test.cpp";
	        String resultFile = "wynik.cpp";
	        CodeParser.addNewlineAfterEachCoutFromFile(filename, resultFile);
	      
	        compiler.CompileNRun(resultFile); // put .cpp file in program directory! (TODO: allow random location)
	        List<String> result = new ArrayList<String>();
	        Ccompiler.runAndReadOutput("test.exe", result, 1);
	        if(!result.isEmpty()){
	        	// do something useful here
	        }
	    }
}
