/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.compiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Pawel
 */
public class Ccompiler {
    
    public void CompileNRun(String filename){

        try {
            Process cmd = new ProcessBuilder("cmd.exe").start(); // start cmd process
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(cmd.getOutputStream()));
            int version = 10; // requires at least VS 2010
            String path = "C:\\Program Files (x86)\\Microsoft Visual Studio " + version + ".0\\VC\\bin";
            File file = new File(path);
            while(!file.isDirectory()) { // check if directory exists
                if(version < 17) {
                    path = "C:\\Program Files (x86)\\Microsoft Visual Studio " + ++version + ".0\\VC\\bin"; // if not, change the VS version and check again
                    file = new File(path);
                }
                else { // if version >= 17 (does it even exist?)
                    System.out.println("Microsoft Visual Studio compiler not found."); // TODO: provide path to compiler and vcvars32
                    return;
                }
            }
            // TODO: check if files exist
            writer.write("\"" + path + "\\vcvars32\""); // load local variables to console process (needed for proper libraries compilation)
            writer.newLine(); // *enter*
            writer.write("\"" + path + "\\cl\" " + filename); // compile file using VS cl compiler
            writer.newLine();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(cmd.getInputStream()));
            writer.flush(); 
            line = reader.readLine();
            while (true) {
                if(line.equals(filename.substring(0, filename.indexOf(".")+1) + "obj ")) // name with .obj is the last compiler returned string - after detection stop reading input
                    break;
                line = reader.readLine();
            }
            cmd.destroy(); // kill process, job is done
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        
        return;
        
    }
    public void CompileNRunOnLinux(String filename){
    	  try {
    		  String[] args = new String[]{"/bin/bash","-c", "g++", "-o", "executable", filename};
              Process cmd = new ProcessBuilder(args).start(); // start cmd process
             
              String line;
              BufferedReader reader = new BufferedReader(new InputStreamReader(cmd.getInputStream()));
              line = reader.readLine();
              while (true) {
                  if(line.equals("")) // cos, co konczy poprawne wykonanie komendy g++
                      break;
                  line = reader.readLine();
              }
              cmd.destroy(); // kill process, job is done
          }
          catch (Exception err) {
              err.printStackTrace();
          }
          
          return;
    }
    
    // uruchamia program executable i wczytuje liczbe expectedLength linii wyjscia do listy output
    static public void runAndReadOutput(String executable, List<String> output, int expectedLength){
        try {
            Process cmd = new ProcessBuilder("cmd.exe").start(); // start cmd process
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(cmd.getOutputStream()));
            Path currentRelativePath = Paths.get("");
 	        String s = currentRelativePath.toAbsolutePath().toString();
 	        s+="\\";
 	        s+=executable;
            File file = new File(s);
            if(file.exists() && file.canExecute()){
            	writer.write(executable);
            	writer.newLine();
            	BufferedReader reader = new BufferedReader(new InputStreamReader(cmd.getInputStream()));
            	writer.flush();
            	String line;
            	line = reader.readLine();
            	line = reader.readLine();
            	line = reader.readLine();
            	line = reader.readLine();
            	for(int i = 0; i < expectedLength; i++){
            		line = reader.readLine();
            		if(line!=null && !line.equals(" ")){
            			output.add(line);
            		}	
            	}
            	writer.close();
            	reader.close();
            }
            cmd.destroy();
        }
        catch(Exception ex){
        	ex.printStackTrace();
        }
    }

	// uruchamia program executable i wczytuje liczbe expectedLength linii wyjscia do listy output
	static public void runAndReadOutputOnLinux(String executable, List<String> output, int expectedLength){
	    
	}
}

