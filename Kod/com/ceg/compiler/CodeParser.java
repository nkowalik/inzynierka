package com.ceg.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CodeParser {
	
	// wczytuje zawartosc pliku ze sciezki pathSrc, dopisuje "cout<<endl" 
	// za kazdym znalezionym wywolaniem "cout" i zapisuje zmodyfikowany kod 
	// do pliku w sciezce pathDest
	// zwraca liczbe znalezionych coutow
	static public int addNewlineAfterEachCout(String pathSrc, String pathDest){
		List<String> lines = new ArrayList<String>();
		int couts = 0;
		try {
			lines = Files.readAllLines(Paths.get(pathSrc));
			for (int index =0; index<lines.size();index++) {
				String line = lines.get(index);
				if(line.contains("cout <<") || line.contains("cout<<")){
					couts++;
					lines.add(++index, "cout<<endl;");
				}
			}
			Files.write(Paths.get(pathDest), lines);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return couts;
	}
	
	// wczytuje zawartosc pliku ze sciezki pathSrc,
	// usuwa kazda linie ze znalezionym wywolaniem "cout<<endl" i zapisuje zmodyfikowany kod 
	// do pliku w sciezce pathDest
	// zwraca liczbe znalezionych coutow
	static public int removeAllNewlines(String pathSrc, String pathDest){
		List<String> lines = new ArrayList<String>();
		int couts = 0;
		try {
			lines = Files.readAllLines(Paths.get(pathSrc));
			for (int index =0; index<lines.size();index++) {
				String line = lines.get(index);
				if(line.contains("cout<<endl;")){
					couts++;
					lines.remove(index);
				}
			}
			Files.write(Paths.get(pathDest), lines);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return couts;
	}
}
